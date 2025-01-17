package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.adapters.ProductAdapter;
import com.example.lako.util.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Accessories_Home extends AppCompatActivity {

    private RecyclerView accessoriesRecyclerView;
    private List<Product> accessoriesList;
    private ProductAdapter accessoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_home);

        // Set OnClickListener for the Home button
        Button homeFragmentButton = findViewById(R.id.home_fragment);
        homeFragmentButton.setOnClickListener(v -> {
            navigateToHomeFragment();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Clothing TextView
        TextView clothingTextView = findViewById(R.id.clothing_acc);
        clothingTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Accessories_Home.this, Clothing_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Living TextView
        TextView livingTextView = findViewById(R.id.living_acc);
        livingTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Accessories_Home.this, Living_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Art TextView
        TextView artTextView = findViewById(R.id.art_cc);
        artTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Accessories_Home.this, Art_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Initialize RecyclerView
        accessoriesRecyclerView = findViewById(R.id.accessories_recycleview);
        accessoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up adapter and data
        accessoriesList = new ArrayList<>();
        accessoriesAdapter = new ProductAdapter(this, accessoriesList, product -> {
            Intent intent = new Intent(Accessories_Home.this, User_View_Product.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
        accessoriesRecyclerView.setAdapter(accessoriesAdapter);

        // Fetch data from Firebase
        fetchAccessoriesFromFirebase();
    }

    private void fetchAccessoriesFromFirebase() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child("ACC_004");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accessoriesList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        accessoriesList.add(product);
                    }
                }
                accessoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void navigateToHomeFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navigate_to", "HomeFragment"); // Pass target fragment name
        startActivity(intent);
        overridePendingTransition(0, 0); // Smooth transition
    }
}
