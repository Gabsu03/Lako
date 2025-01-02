package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.Fragments.Profile_User;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main_Shop_Seller_Categories extends AppCompatActivity {

    private ShapeableImageView profilePictureShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_categories);

        // Initialize the ImageView for the profile picture
        profilePictureShop = findViewById(R.id.profile_picture_shop);

        // Initialize TextViews for shop data
        TextView shopNameTextView = findViewById(R.id.Name_of_seller_shopp);
        TextView shopLocationTextView = findViewById(R.id.name_of_placee);
        TextView shopDescriptionTextView = findViewById(R.id.description_of_shopp);

        // Get current user ID from Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's shop data in Firebase
            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child(userId);

            // Fetch profile image URL and other data from Firebase
            shopRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Retrieve profile image URL
                        String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            // Use Glide to load the profile image
                            Glide.with(Main_Shop_Seller_Categories.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.image_upload)  // Placeholder image
                                    .error(R.drawable.image_upload)  // Error image
                                    .centerCrop()
                                    .into(profilePictureShop);
                        }

                        // Retrieve and set other shop data
                        String shopName = snapshot.child("shopName").getValue(String.class);
                        String shopLocation = snapshot.child("shopLocation").getValue(String.class);
                        String shopDescription = snapshot.child("shopDescription").getValue(String.class);

                        shopNameTextView.setText(shopName != null ? shopName : "Shop Name");
                        shopLocationTextView.setText(shopLocation != null ? shopLocation : "Location");
                        shopDescriptionTextView.setText(shopDescription != null ? shopDescription : "Description");
                    } else {
                        Toast.makeText(Main_Shop_Seller_Categories.this, "No shop data found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Main_Shop_Seller_Categories.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }

        // Ensure proper layout sizing for consistent appearance (Fix layout shifting issues)
        View rootView = findViewById(android.R.id.content);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                // Adjust layout or any necessary parameters after the layout has been drawn
                // Example: Adjust the height of RecyclerView dynamically
                RecyclerView categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
                if (categoriesRecyclerView != null) {
                    categoriesRecyclerView.setVisibility(View.VISIBLE);  // Ensure RecyclerView is visible
                }
            }
        });
    }

    // Navigate to other sections (Product, Orders, etc.)
    public void products_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Products.class));
    }

    public void orders(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Orders.class));
    }

    public void analytics(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Analytics.class));
    }

    public void list_product(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_List_Products.class));
    }

    public void add_categories_seller_btn(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Add_Categories.class));
    }

    public void edit_profile_seller_categories(View view) {
        startActivity(new Intent(Main_Shop_Seller_Categories.this, Main_Shop_Seller_Edit.class));
    }

}

