package com.example.lako;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.PurchaseAdapter;
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
            Toast.makeText(this, "Please log in to view your orders.", Toast.LENGTH_SHORT).show();
            return;
        }

        purchaseRef = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());
        purchaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchaseList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : orderSnapshot.child("items").getChildren()) {
                        CartItem item = itemSnapshot.getValue(CartItem.class);
                        if (item != null) {
                            purchaseList.add(item);
                            System.out.println("DEBUG: Loaded Item - " + item.getName() + " | Seller: " + item.getSellerName());
                        }
                    }
                }
                purchaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_User_Pay.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
