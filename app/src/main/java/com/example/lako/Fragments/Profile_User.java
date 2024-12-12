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

    private boolean isDropdownUp = false; // Track the state of the dropdown icon (up or down)
    private ImageView settingsDrop;
    private TextView nameInput;  // Your TextView for displaying the user's name

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        // Find the ImageView for the settings dropdown by its ID
        settingsDrop = view.findViewById(R.id.settings_drop);
        nameInput = view.findViewById(R.id.nameInput);  // Initialize nameInput TextView

        // Fetch user data from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);

                    // Set the name in the TextView
                    nameInput.setText(firstName + " " + lastName);
                }
            });
        }

        // Set an OnClickListener to the settings dropdown
        settingsDrop.setOnClickListener(v -> {
            // Rotate the dropdown icon (animated rotation)
            animateDropDown(settingsDrop);

            // Toggle the state of the dropdown (up or down)
            isDropdownUp = !isDropdownUp;

            // Start the Profile Settings activity and pass the dropdown state
            Intent intent = new Intent(getActivity(), Profile_Settings.class);
            intent.putExtra("dropdown_state", isDropdownUp); // Pass the state to Profile_Settings
            startActivityForResult(intent, 1); // Request result back from Profile_Settings
        });

        // For Purchase Part
        ImageView imageView4 = view.findViewById(R.id.imageView4);
        imageView4.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile_Settings_Purchase.class));
        });

        // Linked the "to pay" button to purchase
        Button to_pay_btn = view.findViewById(R.id.to_pay);
        to_pay_btn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile_User_Pay.class));
        });

        // Linked the "to ship" button to purchase
        Button to_ship_btn = view.findViewById(R.id.to_ship);
        to_ship_btn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile_User_Ship.class));
        });

        // Linked the "to receive" button to purchase
        Button to_receive_btn = view.findViewById(R.id.to_receive);
        to_receive_btn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile_User_To_Receive.class));
        });

        // Linked the "to review" button to received
        Button received_btn = view.findViewById(R.id.to_review);
        received_btn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile_User_Received.class));
        });

        // Linked the "My Shop" button to start Profile_My_Shop_Start
        TextView shop_btn = view.findViewById(R.id.my_shop_profile_user);
        shop_btn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Profile_My_Shop_Start.class));
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Get the updated dropdown state from the result
            isDropdownUp = data.getBooleanExtra("dropdown_state", false); // Default to down

            // Update the dropdown icon based on the state
            if (isDropdownUp) {
                settingsDrop.setRotation(180f); // Set the "up" rotation
            } else {
                settingsDrop.setRotation(0f); // Set the "down" rotation
            }
        }
    }

    // Method to animate the dropdown icon
    private void animateDropDown(ImageView settingsDrop) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(settingsDrop, "rotation", 0f, 180f);
        rotate.setDuration(300); // Rotate over 300ms
        rotate.start();
    }
}



