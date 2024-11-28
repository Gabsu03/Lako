package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings_Address extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_address);
    }

    // Handle back navigation to Profile_Settings
    public void address_back(View view) {
        // Simply finish this activity to go back to Profile_Settings
        finish();
    }

    public void add_address_btn(View view) {
        startActivity(new Intent(Profile_Settings_Address.this, Profile_Settings_Add_Address.class));
    }
}
