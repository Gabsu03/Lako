package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings_MFA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_mfa);
    }

    // This method is used to go back to Profile_Settings (without recreating the activity stack)
    public void MFA_back(View view) {
        // Use Intent with FLAG_ACTIVITY_CLEAR_TOP to prevent recreating the same activity
        Intent intent = new Intent(Profile_Settings_MFA.this, Profile_Settings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // This ensures the correct activity appears
        startActivity(intent);
    }

    public void drop_down_mfa(View view) {
        Log.d("Profile_Settings_MFA", "drop_down_mfa clicked");
        Intent intent = new Intent(Profile_Settings_MFA.this, Profile_Settings_Add_MFA.class);
        startActivity(intent);
    }

}

