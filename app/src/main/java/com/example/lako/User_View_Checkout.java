package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
    private ArrayList<CartItem> checkoutItems = new ArrayList<>();
    private TextView totalAmountTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_checkout);

        checkoutRecyclerView = findViewById(R.id.list_of_orders_recycleview);
        checkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalAmountTextView = findViewById(R.id.total_amount_checkout);
        checkoutButton = findViewById(R.id.checkout_btn);

        ArrayList<CartItem> receivedItems = getIntent().getParcelableArrayListExtra("checkoutItems");
        if (receivedItems != null) {
            checkoutItems.addAll(receivedItems);
        }

        if (checkoutItems.isEmpty()) {
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

        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(User_View_Checkout.this, User_View_Loading_Screen.class);
            intent.putParcelableArrayListExtra("checkoutItems", checkoutItems);
            startActivity(intent);
            checkoutItems.clear();
            checkoutAdapter.notifyDataSetChanged();
            finish();
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

    private void loadAddresses() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Profile_Settings_Add_Address.Address address = snapshot.getValue(Profile_Settings_Add_Address.Address.class);
                if (address != null) {
                    address.id = snapshot.getKey();
                    addressList.add(address);
                    addressAdapter.notifyItemInserted(addressList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

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
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_View_Checkout.this, "Failed to load addresses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
