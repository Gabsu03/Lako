package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_My_Shop_Verify extends AppCompatActivity {

    private EditText shopNameVerify, shopDescriptionVerify, shopLocationVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_verify);

        // Initialize EditText fields
        shopNameVerify = findViewById(R.id.Shop_name_verify);
        shopDescriptionVerify = findViewById(R.id.descriptionnn);
        shopLocationVerify = findViewById(R.id.Shop_location_verify);

        // Get data from Intent
        String shopName = getIntent().getStringExtra("SHOP_NAME");
        String shopDescription = getIntent().getStringExtra("SHOP_DESCRIPTION");
        String shopLocation = getIntent().getStringExtra("SHOP_LOCATION");

        // Set data to EditTexts
        if (shopName != null) shopNameVerify.setText(shopName);
        if (shopDescription != null) shopDescriptionVerify.setText(shopDescription);
        if (shopLocation != null) shopLocationVerify.setText(shopLocation);

    }


    public void verify_shop_back(View view) {
        startActivity(new Intent(Profile_My_Shop_Verify.this, Profile_My_Shop_Setup.class));
    }

    // TEMPORARY
    public void continue_verify_shop(View view) {
        startActivity(new Intent(Profile_My_Shop_Verify.this, Profile_My_Shop_Facial_Recognition.class));
    }
}


