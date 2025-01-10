package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
        // Get shop details from the EditText fields
        String shopName = shopNameVerify.getText().toString().trim();
        String shopDescription = shopDescriptionVerify.getText().toString().trim();
        String shopLocation = shopLocationVerify.getText().toString().trim();

        if (shopName.isEmpty()) {
            Toast.makeText(this, "Shop name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current seller's Firebase UID
        String sellerId = FirebaseAuth.getInstance().getUid();
        if (sellerId == null) {
            Toast.makeText(this, "Error: Could not get seller ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a reference to the "sellers" node in Firebase
        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("sellers").child(sellerId);

        // Create a map to store seller details
        HashMap<String, String> sellerData = new HashMap<>();
        sellerData.put("name", shopName);
        sellerData.put("description", shopDescription);
        sellerData.put("location", shopLocation);

        // Save the data to Firebase
        sellerRef.setValue(sellerData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Shop details saved successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to the next activity
                Intent intent = new Intent(Profile_My_Shop_Verify.this, Profile_My_Shop_Facial_Recognition.class);
                intent.putExtra("sellerName", shopName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to save shop details. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
