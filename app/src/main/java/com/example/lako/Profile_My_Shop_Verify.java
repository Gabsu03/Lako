package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_My_Shop_Verify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_verify);
    }

    public void verify_shop_back(View view) {
        startActivity(new Intent(Profile_My_Shop_Verify.this, Profile_My_Shop_Setup.class));
    }

    //TEMPORARY
    public void continue_verify_shop(View view) {
        startActivity(new Intent(Profile_My_Shop_Verify.this, Profile_My_Shop_Facial_Recognition.class));
    }
}