package com.example.lako;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.AddressAdapter;
import com.example.lako.util.CartItem;
import com.example.lako.util.CheckoutAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_View_Checkout extends AppCompatActivity {

    private RecyclerView checkoutAddressRecyclerView;
    private RecyclerView checkoutRecyclerView;
    private AddressAdapter addressAdapter;
    private CheckoutAdapter checkoutAdapter;
    private DatabaseReference databaseReference;
    private List<Profile_Settings_Add_Address.Address> addressList = new ArrayList<>();
    private ArrayList<CartItem> checkoutItems;
    private TextView totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_checkout);

        checkoutRecyclerView = findViewById(R.id.list_of_orders_recycleview);
        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalAmountTextView = findViewById(R.id.total_amount_checkout);

        // Fetch data from Intent
        checkoutItems = getIntent().getParcelableArrayListExtra("checkoutItems");
        if (checkoutItems == null || checkoutItems.isEmpty()) {
            Toast.makeText(this, "No items to display.", Toast.LENGTH_SHORT).show();
        } else {
            checkoutAdapter = new CheckoutAdapter(this, checkoutItems);
            checkoutRecyclerView.setAdapter(checkoutAdapter);
            calculateTotalAmountBySeller();
        }

        checkoutAddressRecyclerView = findViewById(R.id.checkout_address_recycler_view);
        checkoutAddressRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addressAdapter = new AddressAdapter(addressList);
        checkoutAddressRecyclerView.setAdapter(addressAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Address-User").child(user.getUid()).child("Address");

        loadAddresses();
    }

    // Calculate and display the total amount, grouped by seller and including shipping fee
    private void calculateTotalAmountBySeller() {
        // Map to store total amount by seller
        Map<String, Double> sellerTotalMap = new HashMap<>();

        // Calculate total for each seller
        for (CartItem item : checkoutItems) {
            try {
                double price = Double.parseDouble(item.getPrice());
                double itemTotal = price * item.getQuantity();

                // Add to the respective seller's total
                String sellerId = item.getSellerId();
                sellerTotalMap.put(sellerId, sellerTotalMap.getOrDefault(sellerId, 0.0) + itemTotal);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price format for item: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        }

        // Now, add shipping fees and calculate the final total for each seller
        double grandTotal = 0.0;
        for (Map.Entry<String, Double> entry : sellerTotalMap.entrySet()) {
            String sellerId = entry.getKey();
            double sellerTotal = entry.getValue();

            // Add a specific shipping fee for each seller
            double shippingFee = getShippingFeeForSeller(sellerId); // You can customize this logic
            sellerTotal += shippingFee;

            // Update the grand total
            grandTotal += sellerTotal;

            // Display seller total with shipping fee
            // Assuming you have a way to display seller-specific totals (e.g., in a RecyclerView or a separate UI element)
            // For now, just log the seller total
            System.out.println("Seller " + sellerId + " Total: " + String.format("₱%.2f", sellerTotal));
        }

        // Update the total amount TextView with the grand total
        totalAmountTextView.setText(String.format("₱%.2f", grandTotal));
    }

    // Method to get the shipping fee for a given seller
    private double getShippingFeeForSeller(String sellerId) {
        // You can customize this to return different fees for different sellers
        // For example, if the sellerId is "seller1", return a specific fee, etc.
        if (sellerId.equals("seller1")) {
            return 45.0; // Example: 45 pesos for seller1
        } else if (sellerId.equals("seller2")) {
            return 50.0; // Example: 50 pesos for seller2
        } else {
            return 45.0; // Default shipping fee
        }
    }

    // Load addresses from Firebase
    private void loadAddresses() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Profile_Settings_Add_Address.Address address = snapshot.getValue(Profile_Settings_Add_Address.Address.class);
                if (address != null) {
                    address.id = snapshot.getKey(); // Set the Firebase key as ID
                    addressList.add(address);
                    addressAdapter.notifyItemInserted(addressList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle changes if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Profile_Settings_Add_Address.Address removedAddress = snapshot.getValue(Profile_Settings_Add_Address.Address.class);
                if (removedAddress != null) {
                    int index = addressList.indexOf(removedAddress);
                    if (index != -1) {
                        addressList.remove(index);
                        addressAdapter.notifyItemRemoved(index);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Optional: Handle move events if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_View_Checkout.this, "Failed to load addresses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
