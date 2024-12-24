package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main_Shop_Seller_Edit_Profile_Loading_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_edit_profile_loading_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is authenticated and navigate accordingly
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                // Start the product screen (same activity for both cases)
                startActivity(new Intent(Main_Shop_Seller_Edit_Profile_Loading_Screen.this, Main_Shop_Seller_Products.class));
                finish();  // Close the loading screen
            }
        }, 2000); // 2-second delay
    }
}

