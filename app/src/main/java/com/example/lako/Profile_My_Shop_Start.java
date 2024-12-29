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
        // Navigate back to MainActivity and clear the stack
        Intent intent = new Intent(Profile_My_Shop_Start.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void set_up_shop_btn(View view) {
        // Navigate to shop setup activity
        startActivity(new Intent(Profile_My_Shop_Start.this, Profile_My_Shop_Setup.class));
    }
}
