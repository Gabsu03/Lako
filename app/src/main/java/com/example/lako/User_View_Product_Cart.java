package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

public class User_View_Product_Cart extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameTextView, priceTextView, descriptionTextView, specificationTextView;
    private TextView sellerNameTextView, sellerLocationTextView;
    private ShapeableImageView sellerProfileImageView;
    private EditText editQuantity;
    private Button buttonDecrease, buttonIncrease;

    private TextView addToCartButton;

    private String productId, productImageUrl, productPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_product_cart);

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

        // Initialize seller views
        sellerNameTextView = findViewById(R.id.seller_name_display_user);
        sellerLocationTextView = findViewById(R.id.seller_location_display_user);
        sellerProfileImageView = findViewById(R.id.profile_picture_seller_display_user);

        // Initialize quantity buttons and add to cart button
        editQuantity = findViewById(R.id.edit_quantity);
        buttonDecrease = findViewById(R.id.button_decrease);
        buttonIncrease = findViewById(R.id.button_increase);
        addToCartButton = findViewById(R.id.add_to_cartt);

        // Get product ID from the intent
        productId = getIntent().getStringExtra("product_id");

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
                                    Intent intent = new Intent(User_View_Product_Cart.this, Seller_View_Profile.class);
                                    intent.putExtra("sellerId", sellerId); // Pass the sellerId to the next activity
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(User_View_Product_Cart.this, "Seller details not available.", Toast.LENGTH_SHORT).show();
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

        // Quantity Buttons Logic
        buttonDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editQuantity.getText().toString());
            if (quantity > 1) {
                editQuantity.setText(String.valueOf(--quantity));
            }
        });

        buttonIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editQuantity.getText().toString());
            editQuantity.setText(String.valueOf(++quantity));
        });


        addToCartButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editQuantity.getText().toString());
            addToCart(productId, quantity); // Call the newly created method
        });

    }

    private void fetchProductDetails(String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(productId);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Productt product = snapshot.getValue(Productt.class);
                    if (product != null) {
                        bindProductDetails(product);

                        // Bind to cart-specific views
                        ImageView imageProductCart = findViewById(R.id.image_product_cart);
                        TextView priceCart = findViewById(R.id.price_cart);

                        Glide.with(User_View_Product_Cart.this)
                                .load(product.getImage()) // Use productImageUrl here if needed
                                .placeholder(R.drawable.image_upload)
                                .into(imageProductCart);

                        priceCart.setText("₱" + product.getPrice()); // Use productPrice here if needed
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to fetch product details: " + error.getMessage());
            }
        });
    }


    private void bindProductDetails(Productt product) {
        nameTextView.setText(product.getName());
        priceTextView.setText("₱" + product.getPrice());
        descriptionTextView.setText(product.getDescription());
        specificationTextView.setText(product.getSpecification());

        Glide.with(User_View_Product_Cart.this)
                .load(product.getImage())
                .placeholder(R.drawable.image_upload)
                .into(imageView);

        productPrice = product.getPrice(); // Store product price
        productImageUrl = product.getImage(); // Store product image URL

        String sellerId = product.getSellerId(); // Get the sellerId from the product
        if (sellerId != null && !sellerId.isEmpty()) {
            fetchSellerDetails(sellerId); // Fetch and display seller details
        } else {
            Log.e("Error", "Seller ID is null or empty.");
        }
    }




    private void fetchSellerDetails(String sellerId) {
        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("shops").child(sellerId);
        sellerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Seller seller = new Seller();
                    seller.setId(snapshot.child("sellerId").getValue(String.class));
                    seller.setName(snapshot.child("shopName").getValue(String.class));
                    seller.setLocation(snapshot.child("shopLocation").getValue(String.class));
                    seller.setProfileImage(snapshot.child("profileImageUrl").getValue(String.class));

                    sellerNameTextView.setText(seller.getName());
                    sellerLocationTextView.setText(seller.getLocation());

                    Glide.with(User_View_Product_Cart.this)
                            .load(seller.getProfileImage())
                            .placeholder(R.drawable.image_upload)
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

    private void proceedToPayment(String productId, int quantity) {
    }


    private void addToCart(String productId, int quantity) {
        String userId = getCurrentUserId(); // Fetch the current user's ID
        if (userId == null) {
            Toast.makeText(this, "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);

        // Fetch product details to get the sellerId
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(productId);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Productt product = snapshot.getValue(Productt.class);
                    if (product != null) {
                        // Create a HashMap for cart item details
                        HashMap<String, Object> cartItem = new HashMap<>();
                        cartItem.put("productId", productId);
                        cartItem.put("quantity", quantity);
                        cartItem.put("price", product.getPrice());
                        cartItem.put("image", product.getImage());
                        cartItem.put("name", product.getName());
                        cartItem.put("sellerId", product.getSellerId());

                        // Add or update the item in the cart
                        cartRef.child(productId).setValue(cartItem)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(User_View_Product_Cart.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(User_View_Product_Cart.this, "Failed to add to cart. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_View_Product_Cart.this, "Failed to fetch product details.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Get current user ID
    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;  // Return null if no user is logged in

    }

    public void cancel_cart_btn(android.view.View view) {
        // Navigate back to the previous screen
        finish();
    }
}