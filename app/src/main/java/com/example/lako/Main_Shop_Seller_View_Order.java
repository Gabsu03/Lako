package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Main_Shop_Seller_View_Order extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, quantityTextView, priceTextView;
    private TextView labelTextView, nameTextView, phoneTextView, fullAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_view_order);

        // Initialize product views
        productImageView = findViewById(R.id.picture_product_detail);
        productNameTextView = findViewById(R.id.name_product_detail);
        quantityTextView = findViewById(R.id.quantity_amount_product_detail);
        priceTextView = findViewById(R.id.price_product_detail);

        // Initialize address views
        labelTextView = findViewById(R.id.label_detail);
        nameTextView = findViewById(R.id.name_detail);
        phoneTextView = findViewById(R.id.phone_detail);
        fullAddressTextView = findViewById(R.id.fullAddress_detail);

        // Retrieve data from intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productImage = intent.getStringExtra("productImage");
        String price = intent.getStringExtra("price");
        int quantity = intent.getIntExtra("quantity", 0);

        // Retrieve address data
        String addressLabel = intent.getStringExtra("addressLabel");
        String addressName = intent.getStringExtra("addressName");
        String addressPhone = intent.getStringExtra("addressPhone");
        String fullAddress = intent.getStringExtra("fullAddress");

        // Set product data to views
        productNameTextView.setText(productName);
        quantityTextView.setText("Qty: " + quantity);
        priceTextView.setText("₱" + price);

        Glide.with(this)
                .load(productImage)
                .placeholder(R.drawable.image_upload)
                .into(productImageView);

        // Set address data to views
        labelTextView.setText(addressLabel != null ? addressLabel : "No Label");
        nameTextView.setText(addressName != null ? addressName : "No Name");
        phoneTextView.setText(addressPhone != null ? addressPhone : "No Phone");
        fullAddressTextView.setText(fullAddress != null ? fullAddress : "No Address");
    }

    public void my_shop_view_orders_back_btn(View view) {
        finish(); // Go back to the previous screen
    }
}
