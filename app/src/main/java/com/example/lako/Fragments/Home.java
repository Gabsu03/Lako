package com.example.lako.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.R;
import com.example.lako.util.Product;
import com.example.lako.util.ProductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private View view;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        productRecyclerView = view.findViewById(R.id.user_products_display_recycler_view);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Product List and Adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, false); // 'false' for user view
        productRecyclerView.setAdapter(productAdapter);

        // Fetch products from database
        fetchProductsFromFirebase();

        return view;
    }

    private void fetchProductsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}
