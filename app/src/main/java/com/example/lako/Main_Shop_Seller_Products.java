package com.example.lako;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lako.Fragments.Profile_User;
import com.example.lako.util.Product;
import com.example.lako.util.ProductAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main_Shop_Seller_Products extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_products);


    }

    public void my_shop_profile_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Profile_User.class));
    }

    public void back_button_shop_seller(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Edit.class));
    }

    public void orders(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Orders.class));
    }

    public void analytics(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Analytics.class));
    }

    public void list_product(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_List_Products.class));
    }

    public void categories_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Categories.class));
    }



    //temporary
    public void my_shop_profile_back_btnn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, TEMPORARY_ACTIVITY.class));
    }
}
