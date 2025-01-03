package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_Settings_Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_settings_payment);
    }


    public void add_payment_btn(View view) {
        startActivity(new Intent(Profile_Settings_Payment.this, Profile_Settings_Add_Payment.class));
    }

    public void payment_back(View view) {
        // Simply finish this activity to go back to Profile_Settings
        finish();
    }
}