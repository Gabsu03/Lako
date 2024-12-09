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

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

public class Profile_My_Shop_Setup extends AppCompatActivity {

    private EditText shopNameInput, shopDescriptionInput, shopLocationInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_setup);

        shopNameInput = findViewById(R.id.shop__name_input);
        shopDescriptionInput = findViewById(R.id.shop__description_input);
        shopLocationInput = findViewById(R.id.shop_location_input);
    }

    public void set_up_back(View view) {
        startActivity(new Intent(Profile_My_Shop_Setup.this, Profile_My_Shop_Start.class));
    }

    public void verify_shop_btn(View view) {
        String shopName = shopNameInput.getText().toString().trim();
        String shopDescription = shopDescriptionInput.getText().toString().trim();
        String shopLocation = shopLocationInput.getText().toString().trim();

        // Validation: Check if any field is empty
        if (shopName.isEmpty()) {
            Toast.makeText(this, "Shop name is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (shopDescription.isEmpty()) {
            Toast.makeText(this, "Description is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (shopLocation.isEmpty()) {
            Toast.makeText(this, "Location is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the shop name contains emojis
        if (containsEmoji(shopName)) {
            Toast.makeText(this, "Shop name cannot contain emojis!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the description contains emojis
        if (containsEmoji(shopDescription)) {
            Toast.makeText(this, "Description cannot contain emojis!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if description exceeds 200 words
        if (wordCount(shopDescription) > 200) {
            Toast.makeText(this, "Description cannot exceed 200 words!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass data to the next activity if validation is successful
        Intent intent = new Intent(Profile_My_Shop_Setup.this, Profile_My_Shop_Verify.class);
        intent.putExtra("SHOP_NAME", shopName);
        intent.putExtra("SHOP_DESCRIPTION", shopDescription);
        intent.putExtra("SHOP_LOCATION", shopLocation);
        startActivity(intent);
    }

    private boolean containsEmoji(String text) {
        // Check if input contains any emoji
        for (char c : text.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && c != '.' && c != ',') {
                return true;
            }
        }
        return false;
    }

    private int wordCount(String text) {
        if (TextUtils.isEmpty(text)) return 0;
        return text.trim().split("\\s+").length;
    }
}


