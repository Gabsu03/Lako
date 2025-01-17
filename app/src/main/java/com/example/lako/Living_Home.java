package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Living_Home extends AppCompatActivity {

    private RecyclerView livingRecyclerView;
    private List<Product> livingList;
    private ProductAdapter livingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_home);

        // Set OnClickListener for the Home button
        Button homeFragmentButton = findViewById(R.id.home_fragment);
        homeFragmentButton.setOnClickListener(v -> {
            navigateToHomeFragment();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Clothing button
        TextView clothingButton = findViewById(R.id.clothing_living);
        clothingButton.setOnClickListener(v -> {
            Intent intent = new Intent(Living_Home.this, Clothing_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Accessories button
        TextView accessoriesButton = findViewById(R.id.accessories_living);
        accessoriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Living_Home.this, Accessories_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Art button
        TextView artButton = findViewById(R.id.art_living);
        artButton.setOnClickListener(v -> {
            Intent intent = new Intent(Living_Home.this, Art_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Initialize RecyclerView
        livingRecyclerView = findViewById(R.id.living_recycleview);
        livingRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up adapter and data
        livingList = new ArrayList<>();
        livingAdapter = new ProductAdapter(this, livingList, product -> {
            Intent intent = new Intent(Living_Home.this, User_View_Product.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
        livingRecyclerView.setAdapter(livingAdapter);

        // Fetch data from Firebase
        fetchLivingFromFirebase();
    }

    private void fetchLivingFromFirebase() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child("LIVING_003");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                livingList.clear(); // Clear existing list
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        livingList.add(product); // Add product to list
                    } else {
                        Log.e("Living_Home", "Null product detected");
                    }
                }
                Log.d("Living_Home", "Products fetched: " + livingList.size());
                livingAdapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Living_Home", "Error fetching data: " + error.getMessage());
            }
        });
    }


    private void navigateToHomeFragment() {
        // Navigate back to the HomeFragment in MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navigate_to", "HomeFragment"); // Pass target fragment name
        startActivity(intent);
        overridePendingTransition(0, 0); // Smooth transition
    }
}
