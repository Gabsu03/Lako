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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main_Shop_Seller_Orders extends AppCompatActivity {

    private TextView amountOrdersTextView;
    private DatabaseReference sellerOrdersRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_orders);

        amountOrdersTextView = findViewById(R.id.amount_orders);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        sellerOrdersRef = FirebaseDatabase.getInstance().getReference("SellerOrders").child(currentUser.getUid());

        fetchPendingOrdersCount();
    }

    private void fetchPendingOrdersCount() {
        sellerOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pendingOrdersCount = 0;
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String status = orderSnapshot.child("status").getValue(String.class);
                    if (status != null && status.equals("To Pay")) {
                        pendingOrdersCount++;
                    }
                }

                amountOrdersTextView.setText(String.valueOf(pendingOrdersCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main_Shop_Seller_Orders.this, "Failed to fetch orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void my_shop_orders_back_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_Products.class));
    }

    public void my_shop_seller_orders(View view) {
        Intent intent = new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_View_Order_List.class);
        startActivity(intent);
    }

    public void my_shop_seller_shipped_out(View view) {
        startActivity(new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_Shipped_Out.class));
    }

    public void my_shop_seller_completed(View view) {
        startActivity(new Intent(Main_Shop_Seller_Orders.this, Main_Shop_Seller_Completed.class));
    }
}
