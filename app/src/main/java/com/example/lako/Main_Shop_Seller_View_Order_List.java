package com.example.lako;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.CheckoutAdapter;
import com.example.lako.util.CartItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main_Shop_Seller_View_Order_List extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private CheckoutAdapter checkoutAdapter;
    private ArrayList<CartItem> ordersList = new ArrayList<>();
    private DatabaseReference ordersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_view_order_list);

        ordersRecyclerView = findViewById(R.id.list_of_orders);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkoutAdapter = new CheckoutAdapter(this, ordersList);
        ordersRecyclerView.setAdapter(checkoutAdapter);

        // Fetch order data from Firebase
        ordersDatabase = FirebaseDatabase.getInstance().getReference("Orders");
        fetchOrders();
    }

    private void fetchOrders() {
        ordersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : orderSnapshot.child("items").getChildren()) {
                        CartItem item = itemSnapshot.getValue(CartItem.class);
                        if (item != null) {
                            ordersList.add(item);
                        }
                    }
                }
                checkoutAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Main_Shop_Seller_View_Order_List.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}