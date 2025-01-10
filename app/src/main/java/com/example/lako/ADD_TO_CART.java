package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.Fragments.Home;
import com.example.lako.util.CartAdapter;
import com.example.lako.util.CartItem;
import com.example.lako.util.CarttAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ADD_TO_CART extends AppCompatActivity {

    private RecyclerView shoppingCartRecyclerView;
    private CarttAdapter cartAdapter;
    private List<CartItem> cartItems;
    private DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_to_cart);

        shoppingCartRecyclerView = findViewById(R.id.shopping_cart);
        shoppingCartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = new ArrayList<>();
        cartAdapter = new CarttAdapter(this, cartItems);
        shoppingCartRecyclerView.setAdapter(cartAdapter);

        loadCartItems();
    }

    private void loadCartItems() {
        String userId = getCurrentUserId();
        if (userId == null) {
            Toast.makeText(this, "Please log in to view your cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem item = dataSnapshot.getValue(CartItem.class);
                    if (item != null && item.getSellerId() != null) { // Ensure sellerId is not null
                        cartItems.add(item);
                    }
                }
                if (cartItems.isEmpty()) {
                    Toast.makeText(ADD_TO_CART.this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                }
                cartAdapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ADD_TO_CART.this, "Failed to load cart items.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void shopping_cart_back_btn(View view) {
        finish();
    }

    private String getCurrentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }
}
