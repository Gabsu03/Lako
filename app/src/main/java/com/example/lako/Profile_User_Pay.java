package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.adapters.PurchaseAdapter;
import com.example.lako.util.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile_User_Pay extends AppCompatActivity {

    private RecyclerView purchaseRecyclerView;
    private PurchaseAdapter purchaseAdapter;
    private List<CartItem> purchaseList;
    private DatabaseReference purchaseRef;
    private ValueEventListener purchaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_user_pay);

        purchaseRecyclerView = findViewById(R.id.purchase_to_pay);
        purchaseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        purchaseList = new ArrayList<>();
        purchaseAdapter = new PurchaseAdapter(this, purchaseList);
        purchaseRecyclerView.setAdapter(purchaseAdapter);

        loadUserPurchases();
    }

    private void loadUserPurchases() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(Profile_User_Pay.this, "Please log in to view your orders.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Profile_User_Pay.this, sign_in.class)); // Navigate to login activity
            finish();
            return;
        }

        purchaseRef = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());

        purchaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchaseList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {

                    String orderId = orderSnapshot.getKey();
                    for (DataSnapshot itemSnapshot : orderSnapshot.child("items").getChildren()) {
                        CartItem item = itemSnapshot.getValue(CartItem.class);
                        if (item != null) {
                            item.setOrderId(orderId);
                            if (itemSnapshot.getKey() != null) { // Null check for key
                                item.setFirebaseKey(itemSnapshot.getKey());
                            }
                            // Set product name and image if available
                            item.setName(itemSnapshot.child("productName").getValue(String.class));
                            item.setImage(itemSnapshot.child("productImage").getValue(String.class));
                            purchaseList.add(item);
                        }
                    }
                }
                purchaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_User_Pay.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        };

        purchaseRef.addListenerForSingleValueEvent(purchaseListener); // Single event listener to optimize resource usage
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (purchaseRef != null && purchaseListener != null) {
                purchaseRef.removeEventListener(purchaseListener);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
    }
}