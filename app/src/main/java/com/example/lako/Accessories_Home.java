package com.example.lako;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.Product;
import com.example.lako.adapters.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

        // BottomNavigationView setup
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_Home);
        bottomNav.setOnItemSelectedListener(navListener);

        // Initialize RecyclerView
        accessoriesRecyclerView = findViewById(R.id.accessories_recycleview);
        accessoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up adapter and data
        accessoriesList = new ArrayList<>();
        accessoriesAdapter = new ProductAdapter(this, accessoriesList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(Accessories_Home.this, User_View_Product.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }
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


    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        // Handle bottom navigation selection
        return true;
    };
}
