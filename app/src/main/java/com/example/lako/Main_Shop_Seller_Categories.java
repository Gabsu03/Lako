package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main_Shop_Seller_Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_categories);
    }

    public void products_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Products.class));
    }

    public void orders(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Orders.class));
    }

    public void analytics(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Analytics.class));
    }

    public void list_product(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_List_Products.class));
    }

    public void add_categories_seller_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Add_Categories.class));
    }


    //TEMPORARY
    public void my_shop_profile_category_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, TEMPORARY_ACTIVITY.class));
    }
}