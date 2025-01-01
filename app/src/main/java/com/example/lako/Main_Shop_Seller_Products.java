package com.example.lako;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.lako.Fragments.Profile_User;
import com.example.lako.util.Product;
import com.example.lako.util.ProductAdapter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main_Shop_Seller_Products extends AppCompatActivity {

    private ShapeableImageView profilePictureShop;
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_products);

        // Initialize the ImageView for the profile picture
        profilePictureShop = findViewById(R.id.profile_picture_shop);
        productsRecyclerView = findViewById(R.id.products_display_recycler_vieww);

        // Load profile picture and shop data
        loadShopData();

        // Load products
        loadProducts();

        // Set up RecyclerView
        setupRecyclerView();
    }

    private void loadShopData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child(userId);
            shopRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(Main_Shop_Seller_Products.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.image_upload)
                                    .error(R.drawable.image_upload)
                                    .centerCrop()
                                    .into(profilePictureShop);
                        }

                        String shopName = snapshot.child("shopName").getValue(String.class);
                        String shopLocation = snapshot.child("shopLocation").getValue(String.class);
                        String shopDescription = snapshot.child("shopDescription").getValue(String.class);

                        TextView nameTextView = findViewById(R.id.Name_of_seller_shop);
                        TextView locationTextView = findViewById(R.id.name_of_place);
                        TextView descriptionTextView = findViewById(R.id.description_of_shop);

                        nameTextView.setText(shopName != null ? shopName : "Shop Name");
                        locationTextView.setText(shopLocation != null ? shopLocation : "Location");
                        descriptionTextView.setText(shopDescription != null ? shopDescription : "Description");
                    } else {
                        Toast.makeText(Main_Shop_Seller_Products.this, "No shop data found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Main_Shop_Seller_Products.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    // Fetch product details
                    String name = productSnapshot.child("name").getValue(String.class);
                    String price = productSnapshot.child("price").getValue(String.class);
                    String image = productSnapshot.child("image").getValue(String.class);
                    String description = productSnapshot.child("description").getValue(String.class);
                    String specification = productSnapshot.child("specification").getValue(String.class);

                    // Get product ID (the key in Firebase)
                    String productId = productSnapshot.getKey();

                    // Create product object with ID and other details
                    Product product = new Product(productId, name, price, image, description, specification);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main_Shop_Seller_Products.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(productList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        productsRecyclerView.setAdapter(productAdapter);
    }

    public void edit_button_shop_seller(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Edit.class));
    }

    public void orders(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Orders.class));
    }

    public void analytics(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Analytics.class));
    }

    public void list_product(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_List_Products.class));
    }

    public void categories_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Categories.class));
    }

    public void my_shop_profile_back_btnn(View view) {
        finish();
    }

    // Add this onActivityResult method to handle the result after product deletion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            boolean productDeleted = data.getBooleanExtra("productDeleted", false);
            if (productDeleted) {
                // If product was deleted, reload the product list
                loadProducts(); // Refresh the product list from Firebase
            }
        }
    }
}



