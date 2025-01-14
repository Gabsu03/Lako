package com.example.lako;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.CartItem;
import com.example.lako.util.CheckoutAdapter;

import java.util.ArrayList;

public class User_View_Checkout extends AppCompatActivity {

    private RecyclerView checkoutRecyclerView;
    private CheckoutAdapter checkoutAdapter;
    private ArrayList<CartItem> checkoutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_checkout);

        checkoutRecyclerView = findViewById(R.id.list_of_orders_recycleview);
        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch data from Intent
        checkoutItems = getIntent().getParcelableArrayListExtra("checkoutItems");
        if (checkoutItems == null || checkoutItems.isEmpty()) {
            Toast.makeText(this, "No items to display.", Toast.LENGTH_SHORT).show();
        } else {
            checkoutAdapter = new CheckoutAdapter(this, checkoutItems);
            checkoutRecyclerView.setAdapter(checkoutAdapter);
        }
    }
}
