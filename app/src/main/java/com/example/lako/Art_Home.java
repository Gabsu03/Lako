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

public class Art_Home extends AppCompatActivity {

    private RecyclerView artRecyclerView;
    private List<Product> artList;
    private ProductAdapter artAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_home);

        // Set OnClickListener for the Home button
        Button homeFragmentButton = findViewById(R.id.home_fragment);
        homeFragmentButton.setOnClickListener(v -> {
            navigateToHomeFragment();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Clothing button
        TextView clothingButton = findViewById(R.id.clothing_Art);
        clothingButton.setOnClickListener(v -> {
            Intent intent = new Intent(Art_Home.this, Clothing_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Living button
        TextView livingButton = findViewById(R.id.living_Art);
        livingButton.setOnClickListener(v -> {
            Intent intent = new Intent(Art_Home.this, Living_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set OnClickListener for Accessories button
        TextView accessoriesButton = findViewById(R.id.accessories_Art);
        accessoriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Art_Home.this, Accessories_Home.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Initialize RecyclerView
        artRecyclerView = findViewById(R.id.art_recycleview);
        artRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up adapter and data
        artList = new ArrayList<>();
        artAdapter = new ProductAdapter(this, artList, product -> {
            Intent intent = new Intent(Art_Home.this, User_View_Product.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
        artRecyclerView.setAdapter(artAdapter);

        // Fetch data from Firebase
        fetchArtFromFirebase();
    }

    private void fetchArtFromFirebase() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child("ART_002"); // Ensure the category ID matches
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                artList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        artList.add(product); // Add product to the list
                    }
                }
                artAdapter.notifyDataSetChanged(); // Notify adapter about data changes
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
