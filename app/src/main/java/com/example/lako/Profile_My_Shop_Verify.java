package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_My_Shop_Verify extends AppCompatActivity {

    private EditText shopNameVerify, shopDescriptionVerify, shopLocationVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_my_shop_verify);

        // Initialize EditText fields
        shopNameVerify = findViewById(R.id.Shop_name_verify);
        shopDescriptionVerify = findViewById(R.id.descriptionnn);
        shopLocationVerify = findViewById(R.id.Shop_location_verify);

        // Get data from Intent (if passed previously)
        String shopName = getIntent().getStringExtra("SHOP_NAME");
        String shopDescription = getIntent().getStringExtra("SHOP_DESCRIPTION");
        String shopLocation = getIntent().getStringExtra("SHOP_LOCATION");

        // Set data to EditTexts
        if (shopName != null) shopNameVerify.setText(shopName);
        if (shopDescription != null) shopDescriptionVerify.setText(shopDescription);
        if (shopLocation != null) shopLocationVerify.setText(shopLocation);
    }

    // When "Back" button is pressed
    public void verify_shop_back(View view) {
        startActivity(new Intent(Profile_My_Shop_Verify.this, Profile_My_Shop_Setup.class));
    }

    // When "Continue" button is pressed
    public void continue_verify_shop(View view) {
        // Get the shop name from the EditText
        String shopName = shopNameVerify.getText().toString();

        // Create an intent to navigate to Profile_My_Shop_Facial_Recognition
        Intent intent = new Intent(Profile_My_Shop_Verify.this, Profile_My_Shop_Facial_Recognition.class);

        // Pass the shop name to the next activity
        intent.putExtra("sellerName", shopName);

        // Start the new activity
        startActivity(intent);
    }
}
