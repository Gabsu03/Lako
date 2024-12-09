package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main_Shop_Seller_Edit_Profile_Loading_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_edit_profile_loading_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    startActivity(new Intent(Main_Shop_Seller_Edit_Profile_Loading_Screen.this, Main_Shop_Seller_Products.class));
                } else {
                    startActivity(new Intent(Main_Shop_Seller_Edit_Profile_Loading_Screen.this, Main_Shop_Seller_Products.class));
                }
                finish();
            }
        },2000);
    }
}
