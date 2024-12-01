package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lako.Fragments.Profile_User;

public class Profile_User_Pay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user_pay);
    }

    // Handle the UI Back button click
    public void pay_purchase_back(View view) {
        // Instead of just finishing the activity, set the BottomNavigationView to Profile_User
        Intent intent = new Intent(Profile_User_Pay.this, MainActivity.class); // Start MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear all activities above it in the stack
        startActivity(intent);
        finish();
    }

    // Override the hardware back button press
    @Override
    public void onBackPressed() {
        // Return to MainActivity, where the Profile_User fragment is part of BottomNavigationView
        Intent intent = new Intent(Profile_User_Pay.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear all activities above it in the stack
        startActivity(intent);
        finish();
    }

    public void All_btn(View view) {
        startActivity(new Intent(Profile_User_Pay.this, Profile_Settings_Purchase.class));
    }

    public void To_Ship(View view) {
        startActivity(new Intent(Profile_User_Pay.this, Profile_User_Ship.class));
    }

    public void To_Receive(View view) {
        startActivity(new Intent(Profile_User_Pay.this, Profile_User_To_Receive.class));
    }

    public void Received(View view) {
        startActivity(new Intent(Profile_User_Pay.this, Profile_User_Received.class));
    }
}




