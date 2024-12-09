package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main_Shop_Seller_Add_Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_add_categories);
    }

    public void my_shop_add_category_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Add_Categories.this, Main_Shop_Seller_Categories.class));
    }
}