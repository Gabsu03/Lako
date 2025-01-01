package com.example.lako;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Main_Shop_Seller_Edit_Product extends AppCompatActivity {

    private EditText editProductName, editProductPrice, editProductDescription, editProductSpecification;
    private ImageView editProductImageView;
    private String productId;
    private DatabaseReference databaseReference;
    private Uri imageUri; // Uri to store the selected image
    private StorageReference storageReference; // Firebase Storage reference
    private String oldImageUrl; // Store the old image URL if it exists

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_edit_product);

        // Initialize views
        editProductName = findViewById(R.id.edit_product_name);
        editProductPrice = findViewById(R.id.edit_product_price);
        editProductDescription = findViewById(R.id.edit_product_description);
        editProductSpecification = findViewById(R.id.edit_product_specification);
        editProductImageView = findViewById(R.id.edit_picture1);

        // Get data passed from the previous activity
        productId = getIntent().getStringExtra("productId");
        String productName = getIntent().getStringExtra("productName");
        String productPrice = getIntent().getStringExtra("productPrice");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productSpecification = getIntent().getStringExtra("productSpecification");
        oldImageUrl = getIntent().getStringExtra("productImageUrl"); // Get the old image URL from the previous activity

        // Set the values to the views
        editProductName.setText(productName);
        editProductPrice.setText(productPrice);
        editProductDescription.setText(productDescription);
        editProductSpecification.setText(productSpecification);

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        storageReference = FirebaseStorage.getInstance().getReference("product_images");

        // Save the edited product
        Button saveButton = findViewById(R.id.edit_product_save_btn);
        saveButton.setOnClickListener(view -> saveEditedProduct());
    }

    // Method to upload the image
    public void uploadProductImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1); // 1 is the request code
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            editProductImageView.setImageURI(imageUri); // Display the selected image
        }
    }

    private void saveEditedProduct() {
        String updatedName = editProductName.getText().toString().trim();
        String updatedPrice = editProductPrice.getText().toString().trim();
        String updatedDescription = editProductDescription.getText().toString().trim();
        String updatedSpecification = editProductSpecification.getText().toString().trim();

        if (updatedName.isEmpty() || updatedPrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image if it's selected
        if (imageUri != null) {
            // Get a reference to Firebase Storage for the new image
            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");

            // If there is an old image, delete it first (optional, but recommended)
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                // Get the reference to the old image and delete it
                StorageReference oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                oldImageRef.delete().addOnSuccessListener(aVoid -> {
                    // After deleting old image, upload the new image
                    uploadNewImage(imageRef, updatedName, updatedPrice, updatedDescription, updatedSpecification);
                }).addOnFailureListener(e -> {
                    // Handle failure to delete the old image
                    Toast.makeText(Main_Shop_Seller_Edit_Product.this, "Failed to delete old image", Toast.LENGTH_SHORT).show();
                    uploadNewImage(imageRef, updatedName, updatedPrice, updatedDescription, updatedSpecification);
                });
            } else {
                // If no old image, just upload the new one
                uploadNewImage(imageRef, updatedName, updatedPrice, updatedDescription, updatedSpecification);
            }
        } else {
            // If no new image, update product without changing the image
            updateProductInDatabase(updatedName, updatedPrice, updatedDescription, updatedSpecification, oldImageUrl);
        }
    }

    private void uploadNewImage(StorageReference imageRef, String updatedName, String updatedPrice,
                                String updatedDescription, String updatedSpecification) {
        // Ensure the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // If there is an old image URL, delete the old image from Firebase Storage
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            StorageReference oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
            oldImageRef.delete().addOnSuccessListener(aVoid -> {
                // After deleting old image, upload the new image
                uploadNewImageToStorage(imageRef, updatedName, updatedPrice, updatedDescription, updatedSpecification);
            }).addOnFailureListener(e -> {
                // Handle failure to delete the old image (optional)
                Toast.makeText(Main_Shop_Seller_Edit_Product.this, "Failed to delete old image", Toast.LENGTH_SHORT).show();
                // Still attempt to upload the new image even if old image deletion failed
                uploadNewImageToStorage(imageRef, updatedName, updatedPrice, updatedDescription, updatedSpecification);
            });
        } else {
            // If there's no old image, directly upload the new image
            uploadNewImageToStorage(imageRef, updatedName, updatedPrice, updatedDescription, updatedSpecification);
        }
    }

    private void uploadNewImageToStorage(StorageReference imageRef, String updatedName, String updatedPrice,
                                         String updatedDescription, String updatedSpecification) {
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newImageUrl = uri.toString();
                        // Update the product data in Firebase Database
                        updateProductInDatabase(updatedName, updatedPrice, updatedDescription, updatedSpecification, newImageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Main_Shop_Seller_Edit_Product.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }


    private void updateProductInDatabase(String updatedName, String updatedPrice, String updatedDescription,
                                         String updatedSpecification, String imageUrl) {
        // Create a map of the updated data
        Map<String, Object> updatedProductData = new HashMap<>();
        updatedProductData.put("name", updatedName);
        updatedProductData.put("price", updatedPrice);
        updatedProductData.put("description", updatedDescription);
        updatedProductData.put("specification", updatedSpecification);
        updatedProductData.put("imageUrl", imageUrl); // Add the new image URL

        // Update the product data in Firebase
        databaseReference.child(productId).updateChildren(updatedProductData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Main_Shop_Seller_Edit_Product.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the edit activity and go back to the previous activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Main_Shop_Seller_Edit_Product.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                });
    }
}



