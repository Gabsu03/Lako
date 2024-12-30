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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_list_products);

        productImageView = findViewById(R.id.imageView51);
        productNameEditText = findViewById(R.id.product_name);
        productDescriptionEditText = findViewById(R.id.product_description);
        productPriceEditText = findViewById(R.id.product_price);
        productSpecificationEditText = findViewById(R.id.product_specification);
        productTagsEditText = findViewById(R.id.product_tags);
        productStockEditText = findViewById(R.id.product_stock);

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

        // Prepare data to save
        String productId = productDatabase.push().getKey();
        Map<String, Object> productData = new HashMap<>();
        productData.put("id", productId);
        productData.put("name", productName);
        productData.put("description", productDescription);
        productData.put("price", productPrice);
        productData.put("specification", productSpecification);
        productData.put("tags", productTags);
        productData.put("stock", productStock);

        if (productImageUri != null) {
            // Upload image to Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("product_images").child(productId + ".jpg");
            storageReference.putFile(productImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Store the image URL in Firebase Realtime Database
                            productData.put("image", imageUrl);
                            // Save product data to Firebase Realtime Database
                            productDatabase.child(productId).setValue(productData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Main_Shop_Seller_List_Products.this, Main_Shop_Seller_Products.class));
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // If no image is selected, store null
            productData.put("image", null);
            // Save product data to Firebase Realtime Database without image
            productDatabase.child(productId).setValue(productData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Main_Shop_Seller_List_Products.this, Main_Shop_Seller_Products.class));
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}




