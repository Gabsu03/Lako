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


public class Clothing_Home extends AppCompatActivity {

    private RecyclerView clothingRecyclerView;
    private List<Product> clothingList;
    private ProductAdapter clothingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_home);

        // Set OnClickListener for the Home button
        Button homeFragmentButton = findViewById(R.id.home_fragment);
        homeFragmentButton.setOnClickListener(v -> {
            navigateToHomeFragment();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Living TextView
        TextView livingTextView = findViewById(R.id.living_clothing);
        livingTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Clothing_Home.this, Living_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Accessories TextView
        TextView accessoriesTextView = findViewById(R.id.accessories_clothing);
        accessoriesTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Clothing_Home.this, Accessories_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Art TextView
        TextView artTextView = findViewById(R.id.art_clothing);
        artTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Clothing_Home.this, Art_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Initialize RecyclerView
        clothingRecyclerView = findViewById(R.id.clothing_recycleview);
        clothingRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up adapter and data
        clothingList = new ArrayList<>();
        clothingAdapter = new ProductAdapter(this, clothingList, product -> {
            Intent intent = new Intent(Clothing_Home.this, User_View_Product.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        clothingRecyclerView.setAdapter(clothingAdapter);

        // Fetch data from Firebase
        fetchClothingFromFirebase();
    }

    private void fetchClothingFromFirebase() {
        // Fetch data from the CLOTH_001 category
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child("CLOTH_001");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clothingList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        clothingList.add(product); // Add product to list
                    }
                }
                clothingAdapter.notifyDataSetChanged(); // Notify adapter about data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
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
