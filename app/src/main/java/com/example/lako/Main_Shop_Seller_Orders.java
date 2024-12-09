package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main_Shop_Seller_Orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_orders);
    }

    public void  my_shop_orders_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_Products.class));
    }

    public void my_shop_seller_orders (View view) {
        startActivity(new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_Amount_Orders.class));
    }

    public void my_shop_seller_shipped_out(View view) {
        startActivity(new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_Shipped_Out.class));
    }

    public void my_shop_seller_completed(View view) {
        startActivity(new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_Completed.class));
    }
}