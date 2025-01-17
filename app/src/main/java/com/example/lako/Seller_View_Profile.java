package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.util.Product;
import com.example.lako.adapters.ProducttAdapter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Seller_View_Profile extends AppCompatActivity {

    private TextView nameTextView, locationTextView, descriptionTextView;
    private ShapeableImageView profilePictureShop;
    private RecyclerView productsRecyclerView;
    private ProducttAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_view_profile);



        // Initialize views
        profilePictureShop = findViewById(R.id.view_profile_seller_shop);
        nameTextView = findViewById(R.id.view_profile_name_of_seller_shop);
        locationTextView = findViewById(R.id.View_profile_name_of_place);
        descriptionTextView = findViewById(R.id.view_profile_description_of_shop);
        productsRecyclerView = findViewById(R.id.user_view_seller_products_recycler_view);

        // Initialize product list
        productList = new ArrayList<>();

        // Debugging logs
        String productId = getIntent().getStringExtra("product_id");
        String sellerId = getIntent().getStringExtra("sellerId");

        Log.d("Seller_View_Profile", "Received product_id: " + productId);
        Log.d("Seller_View_Profile", "Received sellerId: " + sellerId);

        if (sellerId != null) {
            loadSellerDetails(sellerId);
            loadProducts(sellerId);
            setupRecyclerView();
        } else {
            Toast.makeText(this, "Seller ID not found.", Toast.LENGTH_SHORT).show();
        }
    }




    private void loadSellerDetails(String sellerId) {
        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child(sellerId);

        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                    String shopName = snapshot.child("shopName").getValue(String.class);
                    String shopLocation = snapshot.child("shopLocation").getValue(String.class);
                    String shopDescription = snapshot.child("shopDescription").getValue(String.class);

                    // Load profile image
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(Seller_View_Profile.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.image_upload)
                                .error(R.drawable.image_upload)
                                .centerCrop()
                                .into(profilePictureShop);
                    }

                    // Update TextViews
                    nameTextView.setText(shopName != null ? shopName : "Shop Name");
                    locationTextView.setText(shopLocation != null ? shopLocation : "Location");
                    descriptionTextView.setText(shopDescription != null ? shopDescription : "Description");
                } else {
                    Toast.makeText(Seller_View_Profile.this, "Seller details not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Seller_View_Profile.this, "Failed to load seller details.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadProducts(String sellerId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products");

        productRef.orderByChild("sellerId").equalTo(sellerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            String name = productSnapshot.child("name").getValue(String.class);
                            String price = productSnapshot.child("price").getValue(String.class);
                            String image = productSnapshot.child("image").getValue(String.class);
                            String description = productSnapshot.child("description").getValue(String.class);
                            String specification = productSnapshot.child("specification").getValue(String.class);
                            String productId = productSnapshot.getKey();

                            if (name == null) name = "No Name";
                            if (price == null) price = "No Price";
                            if (image == null) image = "";
                            if (description == null) description = "No Description";
                            if (specification == null) specification = "No Specification";

                            Product product = new Product(productId, name, price, image, description, specification);
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Seller_View_Profile.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setupRecyclerView() {
        productAdapter = new ProducttAdapter(this, productList, null);

        // Set up the RecyclerView with a GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        productsRecyclerView.setAdapter(productAdapter);
    }

    public void back_view_profile_seller_btn(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Seller_View_Profile.this, User_View_Product.class);

        // Retrieve and pass productId if available
        String productId = getIntent().getStringExtra("product_id");
        if (productId != null) {
            intent.putExtra("product_id", productId);
        } else {
            Log.w("SellerViewProfile", "Product ID not found when navigating back.");
        }

        startActivity(intent);
        finish();
    }




}
