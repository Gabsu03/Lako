package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings_information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_information);

        // Debug log to confirm activity initialization
        Log.d("ProfileSettings", "Profile_Settings_information activity created");
    }

    public void profile_back(View view) {
        // Navigate back to the previous activity
        finish();  // This will close the current activity and return to Profile_Settings
    }

    public void edit_profile(View view) {
        // Debug log to confirm button click
        Log.d("ProfileSettings", "Edit Profile button clicked");

        // Attempt to navigate to the Profile_Edit activity
        try {
            Intent intent = new Intent(Profile_Settings_information.this, Profile_Edit.class);
            Log.d("ProfileSettings", "Navigating to Profile_Edit activity");
            startActivity(intent);
        } catch (Exception e) {
            Log.e("ProfileSettings", "Error starting Profile_Edit activity", e);
            Toast.makeText(this, "Error navigating to Edit Profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

