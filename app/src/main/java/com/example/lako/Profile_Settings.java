package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings extends AppCompatActivity {

    private boolean isDropdownUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        isDropdownUp = getIntent().getBooleanExtra("dropdown_state", false);
    }

    // Handle the "up" arrow or back button
    public void settings_drop_up(View view) {
        // Toggle the dropdown state before returning
        Intent intent = new Intent();
        intent.putExtra("dropdown_state", !isDropdownUp);
        setResult(RESULT_OK, intent);
        finish(); // Finish this activity to return to Profile_User
    }

    @Override
    public void onBackPressed() {
        // Handle the back press similarly
        Intent intent = new Intent();
        intent.putExtra("dropdown_state", !isDropdownUp);
        setResult(RESULT_OK, intent);
        super.onBackPressed(); // Finish this activity and return to the previous screen
    }

    public void profile_settings(View view) {
        startActivity(new Intent(Profile_Settings.this, Profile_Settings_information.class));
    }

    public void address_settings(View view) {
        startActivity(new Intent(Profile_Settings.this, Profile_Settings_Address.class));
    }

    public void payments_settings(View view) {
        startActivity(new Intent(Profile_Settings.this, Profile_Settings_Payment.class));
    }
}

