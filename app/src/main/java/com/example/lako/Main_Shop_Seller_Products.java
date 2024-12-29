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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop_seller_products);

        // Initialize the ImageView for the profile picture
        profilePictureShop = findViewById(R.id.profile_picture_shop);

        // Get current user ID
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's shop data
            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child(userId);

            // Fetch profile image URL and other data from Firebase
            shopRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Retrieve profile image URL from snapshot
                        String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            // Use Glide to load the profile image into the ImageView
                            Glide.with(Main_Shop_Seller_Products.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.image_upload)  // Placeholder image
                                    .error(R.drawable.image_upload)  // Error image
                                    .into(profilePictureShop);
                        }

                        // Retrieve and set other shop data
                        String shopName = snapshot.child("shopName").getValue(String.class);
                        String shopLocation = snapshot.child("shopLocation").getValue(String.class);
                        String shopDescription = snapshot.child("shopDescription").getValue(String.class);

                        // Initialize TextViews and set the shop data
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



    public void edit_button_shop_seller(View view) {
        // Navigate to seller orders
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Edit.class));
    }

    public void orders(View view) {
        // Navigate to seller orders
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Orders.class));
    }

    public void analytics(View view) {
        // Navigate to analytics page
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Analytics.class));
    }

    public void list_product(View view) {
        // Navigate to product listing page
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_List_Products.class));
    }

    public void categories_btn(View view) {
        // Navigate to categories page
        startActivity(new Intent(Main_Shop_Seller_Products.this, Main_Shop_Seller_Categories.class));
    }

    public void my_shop_profile_back_btnn(View view) {
        finish(); // Go back to previous activity
    }
}

