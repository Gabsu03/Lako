package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Button wishButton;  // Add this for wishlist button
    private boolean isWishlisted = false;  // Track the state of the wishlist button

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

        Button visitProfileBtn = findViewById(R.id.visit_profile_btn);
        visitProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Seller_View_Profile.class);
            String sellerId = "SELLER_ID_FROM_PRODUCT"; // Replace with actual seller ID
            intent.putExtra("sellerId", sellerId);
            startActivity(intent);
        });


        // Initialize product views
        imageView = findViewById(R.id.image_product_display);
        nameTextView = findViewById(R.id.name_product_display_user);
        priceTextView = findViewById(R.id.price_product_display_user);
        descriptionTextView = findViewById(R.id.description_product_display_user);
        specificationTextView = findViewById(R.id.specification_product_display_user);
        Button addToCartButton = findViewById(R.id.add_to_cart_btn);


        // Add click listener to the button
        addToCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(User_View_Product.this, User_View_Product_Cart.class);

            // Pass necessary data, like product ID, to the next activity
            String productId = getIntent().getStringExtra("product_id");
            if (productId != null) {
                intent.putExtra("product_id", productId);
            }

            // Start the User_View_Product_Cart activity
            startActivity(intent);
        });


        // Initialize seller views
        sellerNameTextView = findViewById(R.id.seller_name_display_user);
        sellerLocationTextView = findViewById(R.id.seller_location_display_user);
        sellerProfileImageView = findViewById(R.id.profile_picture_seller_display_user);

        // Initialize wishlist button
        wishButton = findViewById(R.id.wish_product_display_user);  // Your wishlist button

        // Get product ID from the intent
        String productId = getIntent().getStringExtra("product_id");


        if (productId != null) {
            // Fetch product details
            fetchProductDetails(productId);

            // Fetch product details from Firebase and get the sellerId for visiting the profile
            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(productId);

            productRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Productt product = snapshot.getValue(Productt.class);
                        if (product != null) {
                            String sellerId = product.getSellerId(); // Get the seller ID from the product details

                            // Set up the button to navigate to the seller's profile
                            visitProfileBtn.setOnClickListener(v -> {
                                if (sellerId != null && !sellerId.isEmpty()) {
                                    Intent intent = new Intent(User_View_Product.this, Seller_View_Profile.class);
                                    intent.putExtra("sellerId", sellerId); // Pass the sellerId to the next activity
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(User_View_Product.this, "Seller details not available.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Log.e("Firebase", "Product not found in database.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Failed to fetch product details: " + error.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Product ID not found.", Toast.LENGTH_SHORT).show();
        }


        if (productId != null) {
            fetchProductDetails(productId);
            checkIfProductInWishlist(productId);  // Check if the product is in the wishlist
        }

        // Set up the wishlist button click listener
        wishButton.setOnClickListener(v -> {
            if (!isWishlisted) {
                // Mark as wishlisted
                wishButton.setBackgroundResource(R.drawable.wishlist_filled);  // Change to filled star
                isWishlisted = true;
                addToWishlist(productId);  // Add the product to wishlist in Firebase
                Toast.makeText(User_View_Product.this, "Added to Wishlist!", Toast.LENGTH_SHORT).show();
            } else {
                // If already wishlisted, unmark it
                wishButton.setBackgroundResource(R.drawable.wishlist);  // Change to unfilled star
                isWishlisted = false;
                removeFromWishlist(productId);  // Remove the product from wishlist in Firebase
                Toast.makeText(User_View_Product.this, "Removed from Wishlist!", Toast.LENGTH_SHORT).show();
            }
        });
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
                    if (sellerId != null) {
                        fetchSellerDetails(sellerId);
                    }
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
                    sellerNameTextView.setText(seller.getName());  // Ensure this is setting the seller name properly
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


    // Check if the product is already in the wishlist
    private void checkIfProductInWishlist(String productId) {
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists")
                .child(getCurrentUserId()).child(productId);
        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Product is already in the wishlist
                    wishButton.setBackgroundResource(R.drawable.wishlist_filled);  // Set filled star
                    isWishlisted = true;
                } else {
                    // Product is not in the wishlist
                    wishButton.setBackgroundResource(R.drawable.wishlist);  // Set unfilled star
                    isWishlisted = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error checking wishlist: " + error.getMessage());
            }
        });
    }

    private void addToWishlist(String productId) {
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists")
                .child(getCurrentUserId());
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(productId);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Productt product = snapshot.getValue(Productt.class);
                    if (product != null) {
                        // Store the product in the user's wishlist
                        wishlistRef.child(productId).setValue(product)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Broadcast to update the WishList fragment
                                        Intent intent = new Intent("com.yourapp.UPDATE_WISHLIST");
                                        sendBroadcast(intent);  // Notify all registered receivers
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching product: " + error.getMessage());
            }
        });
    }

    private void removeFromWishlist(String productId) {
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists")
                .child(getCurrentUserId());
        wishlistRef.child(productId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Product removed from wishlist");
                        // Broadcast to update the WishList fragment
                        Intent intent = new Intent("com.yourapp.UPDATE_WISHLIST");
                        sendBroadcast(intent);
                    } else {
                        Log.e("Firebase", "Failed to remove product from wishlist");
                    }
                });
    }

    // Get current user ID
    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;  // Return null if no user is logged in
    }

}




