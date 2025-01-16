package com.example.lako;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.CartItem;
import com.example.lako.util.SellerOrdersAdapter; // Use the new adapter
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main_Shop_Seller_View_Order_List extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private SellerOrdersAdapter sellerOrdersAdapter; // Use SellerOrdersAdapter
    private ArrayList<CartItem> ordersList = new ArrayList<>();
    private DatabaseReference ordersDatabase;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_view_order_list);

        // Get seller ID (assuming seller ID is the current logged-in user's UID)
        sellerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ordersRecyclerView = findViewById(R.id.list_of_orders);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the new SellerOrdersAdapter
        sellerOrdersAdapter = new SellerOrdersAdapter(this, ordersList);
        ordersRecyclerView.setAdapter(sellerOrdersAdapter);

        // Fetch orders specific to this seller
        ordersDatabase = FirebaseDatabase.getInstance().getReference("Orders");
        fetchOrdersForSeller();
    }

    private void fetchOrdersForSeller() {
        ordersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String orderId = orderSnapshot.getKey();
                    String orderStatus = orderSnapshot.child("status").getValue(String.class);
                    if ("Pending".equals(orderStatus)) { // Check if order status is "Pending"
                        for (DataSnapshot itemSnapshot : orderSnapshot.child("items").getChildren()) {
                            CartItem item = itemSnapshot.getValue(CartItem.class);
                            if (item != null) {
                                Log.d("FirebaseOrders", "Fetched Item: " + item.toString());
                                if (sellerId.equals(item.getSellerId())) {
                                    item.setOrderId(orderId); // Assign order ID
                                    ordersList.add(item);
                                    Log.d("FirebaseOrders", "Added Item: " + item.getName());
                                }
                            }
                        }
                    }
                }
                sellerOrdersAdapter.notifyDataSetChanged();
                Log.d("FirebaseOrders", "Total Orders: " + ordersList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseOrders", "Error: " + error.getMessage());
                Toast.makeText(Main_Shop_Seller_View_Order_List.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
