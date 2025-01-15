package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.CarttAdapter;
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

public class ADD_TO_CART extends AppCompatActivity {

    private RecyclerView shoppingCartRecyclerView;
    private CarttAdapter cartAdapter;
    private List<CartItem> cartItems;
    private DatabaseReference cartRef, addressRef;
    private TextView totalAmountCart;
    private CheckBox selectAllCheckbox;
    private Button checkoutButton;
    private boolean hasAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_to_cart);

        shoppingCartRecyclerView = findViewById(R.id.shopping_cart);
        shoppingCartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalAmountCart = findViewById(R.id.total_amount_cart);
        selectAllCheckbox = findViewById(R.id.select_all_product);
        checkoutButton = findViewById(R.id.checkout_cart);

        cartItems = new ArrayList<>();
        cartAdapter = new CarttAdapter(this, cartItems, this::updateTotalPrice);
        shoppingCartRecyclerView.setAdapter(cartAdapter);

        selectAllCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartAdapter.selectAllItems(isChecked);
            updateTotalPrice(cartAdapter.calculateTotalPrice());
        });

        checkoutButton.setOnClickListener(v -> checkUserAddressAndProceed());

        loadCartItems();
        checkUserAddress();
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
                    if (item != null) {
                        cartItems.add(item);
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ADD_TO_CART.this, "Failed to load cart items.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserAddress() {
        String userId = getCurrentUserId();
        if (userId == null) return;

        addressRef = FirebaseDatabase.getInstance().getReference("Address-User").child(userId).child("Address");
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hasAddress = snapshot.exists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hasAddress = false;
            }
        });
    }

    private void updateTotalPrice(double totalPrice) {
        totalAmountCart.setText("₱" + totalPrice);
    }

    private void checkUserAddressAndProceed() {
        if (!hasAddress) {
            Toast.makeText(this, "Please add your address first to proceed.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<CartItem> selectedItems = cartAdapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Please select items to checkout.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ADD_TO_CART.this, User_View_Checkout.class);
        intent.putExtra("checkoutItems", new ArrayList<>(selectedItems));
        intent.putExtra("totalPrice", cartAdapter.calculateTotalPrice());
        startActivity(intent);
    }

    public void shopping_cart_back_btn(View view) {
        finish();
    }

    private String getCurrentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }
}
