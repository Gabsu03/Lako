package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings_Add_MFA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings_add_mfa);
    }

    // This method is used to go back to Profile_Settings_MFA
    public void drop_up_mfa(View view) {
        // Use Intent with FLAG_ACTIVITY_CLEAR_TOP to prevent creating the same activity again
        Intent intent = new Intent(Profile_Settings_Add_MFA.this, Profile_Settings_MFA.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Ensures correct activity navigation
        startActivity(intent);
    }
}

