package com.example.lako.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.ADD_TO_CART;
import com.example.lako.About_Us;
import com.example.lako.Logo_Page_Activity2;
import com.example.lako.R;
import com.example.lako.User_View_Product;
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


        // Set GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        // Find the cart button
        Button cartButton = view.findViewById(R.id.cart_btn); // Declare the button only once
        cartButton.setOnClickListener(v -> {
            Log.d("HomeFragment", "Cart button clicked.");
            Intent intent = new Intent(getActivity(), ADD_TO_CART.class);
            startActivity(intent);
        });




        // Initialize Product List and Adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                // Handle product click and start User_View_Product activity
                Intent intent = new Intent(getActivity(), User_View_Product.class);
                intent.putExtra("product_id", product.getId());  // Pass product ID to the activity
                startActivity(intent);
            }
        });

        productRecyclerView.setAdapter(productAdapter);

        // Fetch products from Firebase
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
