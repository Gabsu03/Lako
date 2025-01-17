package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.CartItem;
import com.example.lako.util.CheckoutAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_View_Checkout extends AppCompatActivity {

    private RecyclerView checkoutRecyclerView;
    private CheckoutAdapter checkoutAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<CartItem> checkoutItems = new ArrayList<>();
    private TextView totalAmountTextView, labelTextView, nameTextView, phoneTextView, fullAddressTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_checkout);

        checkoutRecyclerView = findViewById(R.id.list_of_orders_recycleview);
        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalAmountTextView = findViewById(R.id.total_amount_checkout);
        labelTextView = findViewById(R.id.labelTextView_checkout);
        nameTextView = findViewById(R.id.nameTextView_checkout);
        phoneTextView = findViewById(R.id.phoneTextView_checkout);
        fullAddressTextView = findViewById(R.id.fullAddressTextView_checkout);
        checkoutButton = findViewById(R.id.checkout_btn);

        ArrayList<CartItem> receivedItems = getIntent().getParcelableArrayListExtra("checkoutItems");
        if (receivedItems != null) {
            checkoutItems.addAll(receivedItems);
        }

        if (checkoutItems.isEmpty()) {
            Toast.makeText(this, "No items to display.", Toast.LENGTH_SHORT).show();
        } else {
            fetchSellerNames();
            checkoutAdapter = new CheckoutAdapter(this, checkoutItems);
            checkoutRecyclerView.setAdapter(checkoutAdapter);
            calculateTotalAmountBySeller();
        }

        loadUserAddress();

        // Back button click listener
        findViewById(R.id.back_btn).setOnClickListener(v -> finish());

        checkoutButton.setOnClickListener(v -> {
            if (checkoutItems.isEmpty()) {
                Toast.makeText(this, "No items to checkout.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
            DatabaseReference sellerOrdersRef = FirebaseDatabase.getInstance().getReference("SellerOrders");

            String buyerId = user.getUid();
            String orderId = ordersRef.child(buyerId).push().getKey(); // Generate unique order ID

            if (orderId != null) {
                Map<String, Object> orderData = new HashMap<>();
                Map<String, Object> itemsData = new HashMap<>(); // Store all items under the "items" node
                Map<String, Object> addressData = new HashMap<>();

                // Prepare address data
                addressData.put("label", labelTextView.getText().toString());
                addressData.put("name", nameTextView.getText().toString());
                addressData.put("phone", phoneTextView.getText().toString());
                addressData.put("fullAddress", fullAddressTextView.getText().toString());

                Log.d("CheckoutDebug", "Address Data: " + addressData);

                for (CartItem item : checkoutItems) {
                    Map<String, Object> itemDetails = new HashMap<>();
                    itemDetails.put("productId", item.getProductId());
                    itemDetails.put("productName", item.getName());
                    itemDetails.put("productImage", item.getImage());
                    itemDetails.put("price", item.getPrice());
                    itemDetails.put("quantity", item.getQuantity());
                    itemDetails.put("sellerId", item.getSellerId());
                    itemDetails.put("sellerName", item.getSellerName());
                    itemDetails.put("buyerId", buyerId);
                    itemDetails.put("status", "To Pay");
                    itemDetails.put("address", addressData); // Add address to each seller's order


                    // Add each item under the items node
                    itemsData.put(item.getProductId(), itemDetails);

                    // Add data to SellerOrders node
                    sellerOrdersRef.child(item.getSellerId()).child(orderId).setValue(itemDetails);
                }

                // Create order data for buyer
                orderData.put("datePlaced", System.currentTimeMillis());
                orderData.put("status", "To Pay");
                orderData.put("userId", buyerId);
                orderData.put("items", itemsData); // Attach the items map here
                orderData.put("address", addressData); // Add address to the order data

                // Save order under the buyer's node
                ordersRef.child(buyerId).child(orderId).setValue(orderData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(User_View_Checkout.this, User_View_Loading_Screen.class);
                        startActivity(intent);
                        checkoutItems.clear();
                        checkoutAdapter.notifyDataSetChanged();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to place order.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });





    }

        @Override
    protected void onDestroy() {
        super.onDestroy();
        checkoutItems.clear();
        if (checkoutAdapter != null) {
            checkoutAdapter.notifyDataSetChanged();
        }
    }

    private void calculateTotalAmountBySeller() {
        Map<String, Double> sellerTotalMap = new HashMap<>();
        for (CartItem item : checkoutItems) {
            try {
                double price = Double.parseDouble(item.getPrice());
                double itemTotal = price * item.getQuantity();
                String sellerId = item.getSellerId();
                sellerTotalMap.put(sellerId, sellerTotalMap.getOrDefault(sellerId, 0.0) + itemTotal);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price format for item: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        }

        double grandTotal = 0.0;
        for (Map.Entry<String, Double> entry : sellerTotalMap.entrySet()) {
            String sellerId = entry.getKey();
            double sellerTotal = entry.getValue();
            double shippingFee = getShippingFeeForSeller(sellerId);
            sellerTotal += shippingFee;
            grandTotal += sellerTotal;
        }

        totalAmountTextView.setText(String.format("₱%.2f", grandTotal));
    }

    private double getShippingFeeForSeller(String sellerId) {
        if (sellerId.equals("seller1")) {
            return 45.0;
        } else if (sellerId.equals("seller2")) {
            return 50.0;
        } else {
            return 45.0;
        }
    }

    private void loadUserAddress() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference("Address-User").child(user.getUid()).child("Address");
        addressRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                        Profile_Settings_Add_Address.Address address = addressSnapshot.getValue(Profile_Settings_Add_Address.Address.class);
                        if (address != null) {
                            labelTextView.setText(address.getLabel() != null ? address.getLabel() : "No Label");
                            nameTextView.setText(address.getName() != null ? address.getName() : "No Name");
                            phoneTextView.setText(address.getPhone() != null ? address.getPhone() : "No Phone");
                            fullAddressTextView.setText(String.format("%s, %s, %s",
                                    address.getHouseNumber() != null ? address.getHouseNumber() : "No House",
                                    address.getStreet() != null ? address.getStreet() : "No Street",
                                    address.getCity() != null ? address.getCity() : "No City"));
                            break; // Use the first address found
                        }
                    }
                } else {
                    Toast.makeText(User_View_Checkout.this, "No address found. Please add an address.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_View_Checkout.this, "Failed to load address: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchSellerNames() {
        for (CartItem item : checkoutItems) {
            DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("Sellers").child(item.getSellerId());
            sellerRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String sellerName = snapshot.child("name").getValue(String.class);
                        if (sellerName != null) {
                            item.setSellerName(sellerName); // Populate the sellerName
                            checkoutAdapter.notifyDataSetChanged(); // Notify adapter
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Failed to fetch seller name: " + error.getMessage());
                }
            });
        }
    }
}
