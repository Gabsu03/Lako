package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.CartItem;
import com.example.lako.util.SellerCartItem;
import com.example.lako.util.SellerOrdersAdapter; // Use the new adapter
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main_Shop_Seller_View_Order_List extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private SellerOrdersAdapter ordersAdapter;
    private List<SellerCartItem> ordersList;
    private DatabaseReference sellerOrdersRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_view_order_list);

        ordersRecyclerView = findViewById(R.id.list_of_orders);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordersList = new ArrayList<>();
        ordersAdapter = new SellerOrdersAdapter(this, ordersList);
        ordersRecyclerView.setAdapter(ordersAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        sellerOrdersRef = FirebaseDatabase.getInstance().getReference("SellerOrders").child(currentUser.getUid());

        fetchSellerOrders();
    }

    private void fetchSellerOrders() {
        sellerOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    SellerCartItem item = orderSnapshot.getValue(SellerCartItem.class);
                    if (item != null) {
                        // Add address to the item
                        DataSnapshot addressSnapshot = orderSnapshot.child("address");
                        if (addressSnapshot.exists()) {
                            String label = addressSnapshot.child("label").getValue(String.class);
                            String name = addressSnapshot.child("name").getValue(String.class);
                            String phone = addressSnapshot.child("phone").getValue(String.class);
                            String fullAddress = addressSnapshot.child("fullAddress").getValue(String.class);

                            item.setAddressLabel(label);
                            item.setAddressName(name);
                            item.setAddressPhone(phone);
                            item.setFullAddress(fullAddress);
                        }
                        ordersList.add(item);
                    }
                }
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main_Shop_Seller_View_Order_List.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void my_shop_view_order_list_back_btn(View view) {
        Intent intent = new Intent(Main_Shop_Seller_View_Order_List.this, Main_Shop_Seller_Orders.class);
        startActivity(intent);
        finish(); // Ensure the current activity is closed
    }

}



