package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile_My_Shop_Setup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_setup);
    }

    public void set_up_back(View view) {
        startActivity(new Intent(Profile_My_Shop_Setup.this, Profile_My_Shop_Start.class));
    }

    public void verify_shop_btn(View view) {
        startActivity(new Intent(Profile_My_Shop_Setup.this, Profile_My_Shop_Verify.class));
    }

}