package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lako.Fragments.Profile_User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_My_Shop_Loading_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_loading_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    // If the user is not logged in, redirect to the main shop seller products
                    startActivity(new Intent(Profile_My_Shop_Loading_Screen.this, Main_Shop_Seller_Products.class));
                } else {
                    // If the user is logged in, directly navigate to the Profile_User fragment
                    // Start a new activity that contains the Profile_User fragment
                    Intent intent = new Intent(Profile_My_Shop_Loading_Screen.this, MainActivity.class); // Change to your main activity
                    intent.putExtra("showProfileUser", true); // Pass extra to show Profile_User
                    startActivity(intent);
                }
                finish();
            }
        }, 2000); // Delay for 2 seconds before checking user status
    }
}
