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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings);
// Get the dropdown state from the intent
        isDropdownUp = getIntent().getBooleanExtra("dropdown_state", false); // Default is false (down)

        // Optionally, do something with the state, like displaying it or updating UI elements
    }

    public void settings_drop_up(View view) {
        // When user clicks to go back, set the dropdown state before finishing
        Intent intent = new Intent();
        intent.putExtra("dropdown_state", !isDropdownUp); // Toggle the state before returning
        setResult(RESULT_OK, intent); // Return the result to the calling activity (Profile_User)
        finish();
    }

    @Override
    public void onBackPressed() {
        // Handle the back press similarly
        Intent intent = new Intent();
        intent.putExtra("dropdown_state", !isDropdownUp); // Toggle the state before returning
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
    public void profile_settings(View view) {
        startActivity(new Intent(Profile_Settings.this, Profile_Settings_information.class));
    }
}