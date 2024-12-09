package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main_Shop_Seller_Amount_Orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_amount_orders);
    }

    public void my_shop_amount_orders_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Amount_Orders.this, Main_Shop_Seller_Orders.class));
    }

    public void my_shop_seller_view_order_button(View view) {
        startActivity(new Intent(Main_Shop_Seller_Amount_Orders.this, Main_Shop_Seller_View_Order.class));
    }
}