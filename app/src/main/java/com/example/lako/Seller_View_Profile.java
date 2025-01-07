package com.example.lako;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Seller_View_Profile extends AppCompatActivity {

    private TextView nameTextView, locationTextView, descriptionTextView;
    private ShapeableImageView profilePictureShop;

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

        // Get sellerId from Intent
        String sellerId = getIntent().getStringExtra("sellerId");
        if (sellerId != null) {
            loadSellerDetails(sellerId);
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
}
