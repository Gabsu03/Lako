package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Main_Shop_Seller_View_Product extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, productSpecificationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_view_product);

        // Initialize the views
        productImageView = findViewById(R.id.picture_of_product);
        productNameTextView = findViewById(R.id.name_of_product_display);
        productPriceTextView = findViewById(R.id.price);
        productDescriptionTextView = findViewById(R.id.description_product);
        productSpecificationTextView = findViewById(R.id.specification_of_product);  // Initialize specification view

        // Get the product data from the intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productPrice = intent.getStringExtra("productPrice");
        String productImage = intent.getStringExtra("productImage");
        String productDescription = intent.getStringExtra("productDescription"); // Get description
        String productSpecification = intent.getStringExtra("productSpecification"); // Get specification

        // Set the product data to the views
        if (productName != null && productPrice != null) {
            productNameTextView.setText(productName);
            productPriceTextView.setText("₱" + productPrice);
        }

        if (productImage != null && !productImage.isEmpty()) {
            Glide.with(this)
                    .load(productImage)
                    .placeholder(R.drawable.image_upload)
                    .into(productImageView);
        }

        // Set description and specification if available
        if (productDescription != null && !productDescription.isEmpty()) {
            productDescriptionTextView.setText(productDescription);
        } else {
            productDescriptionTextView.setText("No description available.");
        }

        if (productSpecification != null && !productSpecification.isEmpty()) {
            productSpecificationTextView.setText(productSpecification);
        } else {
            productSpecificationTextView.setText("No specifications available.");
        }
    }
}