package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings_information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings_information);

    }

    public void profile_back(View view) {
        startActivity(new Intent(Profile_Settings_information.this, Profile_Settings.class));
    }

    public void edit_profile(View view) {
        startActivity(new Intent(Profile_Settings_information.this, Profile_Edit.class));
    }
}