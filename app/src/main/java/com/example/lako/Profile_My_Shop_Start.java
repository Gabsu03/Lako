package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_My_Shop_Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_start);
    }

    public void my_shop_back(View view) {
        // Instead of just finishing the activity, set the BottomNavigationView to Profile_User
        Intent intent = new Intent(Profile_My_Shop_Start.this, MainActivity.class); // Start MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear all activities above it in the stack
        startActivity(intent);
        finish();
    }

    public void set_up_shop_btn(View view) {
        startActivity(new Intent(Profile_My_Shop_Start.this, Profile_My_Shop_Setup.class));
    }
}