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

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_products);

        recyclerView = findViewById(R.id.products_display_recycler_vieww);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load product data from SharedPreferences
        loadProductData();

        // Set up the adapter
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProductData() {
        SharedPreferences sharedPreferences = getSharedPreferences("ProductData", MODE_PRIVATE);
        String json = sharedPreferences.getString("products", "[]");

        // Parse the JSON string into a list of Product objects
        Gson gson = new Gson();
        Type type = new TypeToken<List<Product>>() {}.getType();
        List<Product> loadedProductList = gson.fromJson(json, type);

        // Check if the loaded list is not empty
        if (loadedProductList != null && !loadedProductList.isEmpty()) {
            productList.addAll(loadedProductList);
        } else {
            Log.d("Main_Shop_Seller_Products", "No products found in SharedPreferences.");
        }

        // Notify the adapter that the data has changed
        productAdapter.notifyDataSetChanged();
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
