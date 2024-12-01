package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings_Purchase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_purchase);
    }

    public void purchase_back(View view) {
        // Navigate to MainActivity, which will ensure Profile_User fragment is shown
        Intent intent = new Intent(Profile_Settings_Purchase.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear all activities above it in the stack
        startActivity(intent);
        finish();
    }

    // Handle other button actions
    public void To_Pay(View view) {
        startActivity(new Intent(Profile_Settings_Purchase.this, Profile_User_Pay.class));
    }

    public void To_Ship(View view) {
        startActivity(new Intent(Profile_Settings_Purchase.this, Profile_User_Ship.class));
    }

    public void To_Receive(View view) {
        startActivity(new Intent(Profile_Settings_Purchase.this, Profile_User_To_Receive.class));
    }

    public void Received(View view) {
        startActivity(new Intent(Profile_Settings_Purchase.this, Profile_User_Received.class));
    }
}

