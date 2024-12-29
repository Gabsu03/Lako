package com.example.lako.Fragments;

import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lako.Main_Shop_Seller_Products;
import com.example.lako.Profile_My_Shop_Start;
import com.example.lako.Profile_Settings;
import com.example.lako.Profile_Settings_Purchase;
import com.example.lako.Profile_User_Pay;
import com.example.lako.Profile_User_Received;
import com.example.lako.Profile_User_Ship;
import com.example.lako.sign_in;
import com.example.lako.Profile_User_To_Receive;
import com.example.lako.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_User extends Fragment {

    private boolean isDropdownUp = false;
    private ImageView settingsDrop;
    private TextView nameInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        // Initialize views
        settingsDrop = view.findViewById(R.id.settings_drop);
        nameInput = view.findViewById(R.id.nameInput);

        // Fetch user data from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    nameInput.setText(firstName + " " + lastName);
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

    private void setupPurchaseButtons(View view) {
        view.findViewById(R.id.to_pay).setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile_User_Pay.class)));
        view.findViewById(R.id.to_ship).setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile_User_Ship.class)));
        view.findViewById(R.id.to_receive).setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile_User_To_Receive.class)));
        view.findViewById(R.id.to_review).setOnClickListener(v -> startActivity(new Intent(getActivity(), Profile_User_Received.class)));
    }

    private void navigateToShop(FirebaseUser currentUser) {
        if (currentUser != null) {
            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Shops").child(currentUser.getUid());
            shopRef.get().addOnCompleteListener(shopTask -> {
                if (shopTask.isSuccessful()) {
                    DataSnapshot shopSnapshot = shopTask.getResult();
                    // Check if the user has a seller account
                    if (shopSnapshot.exists() && Boolean.TRUE.equals(shopSnapshot.child("sellerAccount").getValue(Boolean.class))) {
                        // Seller account exists, navigate to the seller profile
                        startActivity(new Intent(getActivity(), Main_Shop_Seller_Products.class));
                    } else {
                        // No seller account, navigate to shop setup
                        startActivity(new Intent(getActivity(), Profile_My_Shop_Start.class));
                    }
                }
            });
        }
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






