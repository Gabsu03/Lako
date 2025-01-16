package com.example.lako.Fragments;

import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lako.Main_Shop_Seller_Products;
import com.example.lako.Profile_My_Shop_Start;
import com.example.lako.Profile_Settings;
import com.example.lako.Profile_User_Pay;
import com.example.lako.Profile_User_Ship;
import com.example.lako.Profile_User_To_Receive;
import com.example.lako.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Profile_User extends Fragment {

    private boolean isDropdownUp = false;
    private ImageView settingsDrop;
    private TextView nameInput;
    private ShapeableImageView profileImageView; // For displaying the profile image

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        // Initialize views
        settingsDrop = view.findViewById(R.id.settings_drop);
        nameInput = view.findViewById(R.id.nameInput);
        profileImageView = view.findViewById(R.id.UploadImage); // The ImageView to display the profile picture

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

            // Add a real-time listener
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImage").getValue(String.class);

                    // Update username
                    nameInput.setText(username != null ? username : "Username");

                    // Update profile image
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(requireActivity())
                                .load(profileImageUrl)
                                .placeholder(R.drawable.image_upload)
                                .error(R.drawable.image_upload)
                                .centerCrop()
                                .into(profileImageView);
                    } else {
                        profileImageView.setImageResource(R.drawable.image_upload);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        // Settings dropdown click listener
        settingsDrop.setOnClickListener(v -> {
            animateDropDown(settingsDrop);
            isDropdownUp = !isDropdownUp;
            Intent intent = new Intent(getActivity(), Profile_Settings.class);
            intent.putExtra("dropdown_state", isDropdownUp);
            startActivityForResult(intent, 1);
        });

        // Other button actions
        setupPurchaseButtons(view);

        // "My Shop" button to navigate based on seller account status
        TextView shopBtn = view.findViewById(R.id.my_shop_profile_user);
        shopBtn.setOnClickListener(v -> navigateToShop(currentUser));

        return view;
    }

    // Helper method to generate a default username
    private String generateDefaultUsername() {
        String consonants = "BCDFGHJKLMNPQRSTVWXYZ"; // String containing consonants
        StringBuilder usernameBuilder = new StringBuilder();
        Random random = new Random();

        // Generate 6 random consonants
        for (int i = 0; i < 6; i++) {
            char randomConsonant = consonants.charAt(random.nextInt(consonants.length()));
            usernameBuilder.append(randomConsonant);
        }

        // Add a single random digit (0-9)
        int randomDigit = random.nextInt(10);
        usernameBuilder.append(randomDigit);

        return usernameBuilder.toString();
    }


    private void setupPurchaseButtons(View view) {
        view.findViewById(R.id.to_pay).setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile_User_Pay.class)));
        view.findViewById(R.id.to_ship).setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile_User_Ship.class)));
        view.findViewById(R.id.to_receive).setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile_User_To_Receive.class)));
    }

    private void navigateToShop(FirebaseUser currentUser) {
        if (currentUser == null) {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Shops").child(currentUser.getUid());

        // Add a real-time listener to check the shop data
        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot shopSnapshot) {
                // Check if the shop data exists
                if (shopSnapshot.exists()) {
                    Boolean isSellerAccount = shopSnapshot.child("sellerAccount").getValue(Boolean.class);

                    if (Boolean.TRUE.equals(isSellerAccount)) {
                        // If the shop exists and is a seller account, proceed to seller profile
                        Intent intent = new Intent(getActivity(), Main_Shop_Seller_Products.class);
                        startActivity(intent);
                    } else {
                        // If the shop data exists but is not marked as sellerAccount, redirect to setup
                        Toast.makeText(getActivity(), "Shop setup incomplete. Redirecting to setup.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), Profile_My_Shop_Start.class);
                        startActivity(intent);
                    }
                } else {
                    // If the shop data does not exist, redirect to setup
                    Toast.makeText(getActivity(), "No shop data found. Redirecting to shop setup.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Profile_My_Shop_Start.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to connect to database: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            isDropdownUp = data.getBooleanExtra("dropdown_state", false);
            settingsDrop.setRotation(isDropdownUp ? 180f : 0f);
        }
    }

    private void animateDropDown(ImageView settingsDrop) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(settingsDrop, "rotation", 0f, 180f);
        rotate.setDuration(300);
        rotate.start();
    }
}







