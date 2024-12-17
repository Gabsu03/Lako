package com.example.lako;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;

public class Main_Shop_Seller_List_Products extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView productImageView;
    private EditText productNameEditText, productDescriptionEditText, productPriceEditText, productSpecificationEditText, productTagsEditText;
    private Uri productImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_list_products);

        productImageView = findViewById(R.id.imageView51);
        productNameEditText = findViewById(R.id.product_name);
        productDescriptionEditText = findViewById(R.id.product_description);
        productPriceEditText = findViewById(R.id.product_price);
        productSpecificationEditText = findViewById(R.id.product_specification);
        productTagsEditText = findViewById(R.id.product_tags);

        TextView addProductButton = findViewById(R.id.add_list_product_btn);
        addProductButton.setOnClickListener(view -> uploadProductToFirebase());
    }

    public void my_shop_list_product_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_List_Products.this, Main_Shop_Seller_Products.class));
    }

    public void uploadProductImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            productImageUri = data.getData();
            productImageView.setImageURI(productImageUri);
        }
    }

    private void uploadProductToFirebase() {
        // Retrieve product details
        String productName = productNameEditText.getText().toString().trim();
        String productDescription = productDescriptionEditText.getText().toString().trim();
        String productPrice = productPriceEditText.getText().toString().trim();
        String productSpecification = productSpecificationEditText.getText().toString().trim();
        String productTags = productTagsEditText.getText().toString().trim();

        if (productImageUri == null || productName.isEmpty() || productPrice.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference for Firebase Storage
        String imagePath = "products/" + System.currentTimeMillis() + ".jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(imagePath);

        // Upload image to Firebase Storage
        storageReference.putFile(productImageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            // Save product data to Firebase Database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = database.getReference("products");

                            String productId = databaseReference.push().getKey(); // Generate unique product ID
                            Product product = new Product(productId, productName, productDescription, productPrice, productSpecification, productTags, imageUrl);

                            databaseReference.child(productId).setValue(product)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(this, "Product uploaded successfully", Toast.LENGTH_SHORT).show();
                                            // Navigate to the next activity
                                            startActivity(new Intent(Main_Shop_Seller_List_Products.this, Main_Shop_Seller_Save_Screen.class));
                                        } else {
                                            Toast.makeText(this, "Failed to upload product", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
                });
    }

}


