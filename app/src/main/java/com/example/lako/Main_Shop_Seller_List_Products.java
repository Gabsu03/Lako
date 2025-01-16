package com.example.lako;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lako.util.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main_Shop_Seller_List_Products extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView productImageView;
    private EditText productNameEditText, productDescriptionEditText, productPriceEditText, productSpecificationEditText, productTagsEditText, productStockEditText;
    private Uri productImageUri;
    private DatabaseReference productDatabase;
    private ProgressBar categoryProgressBar; // Declare ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_list_products);

        // Initialize views
        productImageView = findViewById(R.id.imageView51);
        productNameEditText = findViewById(R.id.product_name);
        productDescriptionEditText = findViewById(R.id.product_description);
        productPriceEditText = findViewById(R.id.product_price);
        productSpecificationEditText = findViewById(R.id.product_specification);
        productTagsEditText = findViewById(R.id.product_tags);
        productStockEditText = findViewById(R.id.product_stock);

        // Initialize ProgressBar
        categoryProgressBar = findViewById(R.id.category_progress_bar);

        Button addProductButton = findViewById(R.id.add_list_product_btn);
        productDatabase = FirebaseDatabase.getInstance().getReference("products");

        addProductButton.setOnClickListener(view -> saveProductToFirebase());
    }

    public void uploadProductImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            productImageUri = data.getData();
            productImageView.setImageURI(productImageUri);
        }
    }

    private void saveProductToFirebase() {
        String productName = productNameEditText.getText().toString().trim();
        String productDescription = productDescriptionEditText.getText().toString().trim();
        String productPrice = productPriceEditText.getText().toString().trim();
        String productSpecification = productSpecificationEditText.getText().toString().trim();
        String productTags = productTagsEditText.getText().toString().trim();
        String productStock = productStockEditText.getText().toString().trim();

        if (productName.isEmpty() || productPrice.isEmpty() || productStock.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show ProgressBar while processing
        categoryProgressBar.setVisibility(View.VISIBLE);

        // Generate product ID
        String productId = productDatabase.push().getKey();
        if (productId == null) {
            Toast.makeText(this, "Failed to generate product ID", Toast.LENGTH_SHORT).show();
            categoryProgressBar.setVisibility(View.GONE); // Hide ProgressBar
            return;
        }

        // Get the current user's ID (sellerId)
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You need to log in before adding products.", Toast.LENGTH_SHORT).show();
            categoryProgressBar.setVisibility(View.GONE);
            return;
        }
        String sellerId = currentUser.getUid();

        // Prepare product data to save
        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productId);
        productData.put("name", productName);
        productData.put("description", productDescription);
        productData.put("price", productPrice);
        productData.put("specification", productSpecification);
        productData.put("tags", productTags);
        productData.put("stock", productStock);
        productData.put("sellerId", sellerId);

        // If the product has an image, upload it and save the URL
        if (productImageUri != null) {
            // Upload image to Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("product_images").child(productId + ".jpg");
            storageReference.putFile(productImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            productData.put("image", imageUrl); // Store image URL in product data

                            // Save product data to Firebase
                            saveProductDataToFirebase(productId, sellerId, productData);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            categoryProgressBar.setVisibility(View.GONE);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        categoryProgressBar.setVisibility(View.GONE);
                    });
        } else {
            productData.put("image", null);

            productData.put("id", productId);

            // Save product data to Firebase without image
            saveProductDataToFirebase(productId, sellerId, productData);
        }
    }

    private void saveProductDataToFirebase(String productId, String sellerId, Map<String, Object> productData) {
        // Save product to the "products" node
        productDatabase.child(productId).setValue(productData);

        // Save product to the seller's "products" node
        DatabaseReference sellerProductsRef = FirebaseDatabase.getInstance().getReference("sellers").child(sellerId).child("products");
        sellerProductsRef.child(productId).setValue(productData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                    categoryProgressBar.setVisibility(View.GONE);

                    // Navigate to the Main_Shop_Seller_Products and clear back stack
                    startActivity(new Intent(Main_Shop_Seller_List_Products.this, Main_Shop_Seller_Products.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish(); // Ensure the current activity is removed from the back stack
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    categoryProgressBar.setVisibility(View.GONE);
                });
    }




    public void my_shop_list_product_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_List_Products.this, Main_Shop_Seller_Products.class));
    }
}







