package com.example.lako;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.lako.util.Product;
import com.example.lako.util.Productt;
import com.example.lako.util.Seller;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_View_Product extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTextView, priceTextView, descriptionTextView, specificationTextView;
    private TextView sellerNameTextView, sellerLocationTextView;
    private ShapeableImageView sellerProfileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_product);

        // Button back click listener
        Button backButton = findViewById(R.id.view_product_back_btn);
        backButton.setOnClickListener(v -> {
            // Finish activity and go back to Home fragment
            finish();
        });

        // Initialize product views
        imageView = findViewById(R.id.image_product_display);
        nameTextView = findViewById(R.id.name_product_display_user);
        priceTextView = findViewById(R.id.price_product_display_user);
        descriptionTextView = findViewById(R.id.description_product_display_user);
        specificationTextView = findViewById(R.id.specification_product_display_user);

        // Initialize seller views
        sellerNameTextView = findViewById(R.id.seller_name_display_user);
        sellerLocationTextView = findViewById(R.id.seller_location_display_user);
        sellerProfileImageView = findViewById(R.id.profile_picture_seller_display_user);

        // Get product ID from the intent
        String productId = getIntent().getStringExtra("product_id");

        if (productId != null) {
            fetchProductDetails(productId);
        }
    }

    private void fetchProductDetails(String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(productId);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Productt product = snapshot.getValue(Productt.class);
                if (product != null) {
                    nameTextView.setText(product.getName());
                    priceTextView.setText("₱" + product.getPrice());
                    descriptionTextView.setText(product.getDescription());
                    specificationTextView.setText(product.getSpecification());

                    Glide.with(User_View_Product.this)
                            .load(product.getImage())
                            .placeholder(R.drawable.image_upload)
                            .into(imageView);

                    String sellerId = product.getSellerId();
                    Log.d("Firebase", "Seller ID: " + sellerId);
                    if (sellerId != null) {
                        fetchSellerDetails(sellerId);
                    } else {
                        Log.e("Firebase", "Seller ID is null.");
                    }
                } else {
                    Log.e("Firebase", "Product data is null.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching product: " + error.getMessage());
            }
        });
    }

    private void fetchSellerDetails(String sellerId) {
        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("shops").child(sellerId);

        // Use addValueEventListener for real-time updates
        sellerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Seller seller = new Seller();
                    seller.setId(snapshot.child("sellerId").getValue(String.class));
                    seller.setName(snapshot.child("shopName").getValue(String.class));
                    seller.setLocation(snapshot.child("shopLocation").getValue(String.class));
                    seller.setProfileImage(snapshot.child("profileImageUrl").getValue(String.class));

                    // Update UI with seller details
                    sellerNameTextView.setText(seller.getName());
                    sellerLocationTextView.setText(seller.getLocation());

                    // Load updated profile image using Glide
                    Glide.with(User_View_Product.this)
                            .load(seller.getProfileImage())
                            .placeholder(R.drawable.image_upload)  // Placeholder image
                            .error(R.drawable.image_upload)  // Error image
                            .centerCrop()
                            .into(sellerProfileImageView);
                } else {
                    Log.e("Firebase", "Seller not found for ID: " + sellerId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching seller: " + error.getMessage());
            }
        });
    }


}

