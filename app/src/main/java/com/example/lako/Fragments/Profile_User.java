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
import com.example.lako.Profile_User_To_Receive;
import com.example.lako.R;

public class Profile_User extends Fragment {

    private boolean isDropdownUp = false; // Track the state of the dropdown icon (up or down)
    private ImageView settingsDrop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        // Find the ImageView for the settings dropdown by its ID
        settingsDrop = view.findViewById(R.id.settings_drop);

        // Set an OnClickListener to the ImageView
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

        // Set an OnClickListener to the ImageView
        imageView4.setOnClickListener(v -> {
            // Start the Profile_Settings_Purchase activity when clicked
            startActivity(new Intent(getActivity(), Profile_Settings_Purchase.class));
        });


        //Linked the to pay symbol to purchase
        Button to_pay_btn = view.findViewById(R.id.to_pay);

        // Set an OnClickListener to the ImageView
        to_pay_btn.setOnClickListener(v -> {
            // Start the Profile_Settings_Purchase activity when clicked
            startActivity(new Intent(getActivity(), Profile_User_Pay.class));
        });

        //Linked the to ship symbol to purchase
        Button to_ship_btn = view.findViewById(R.id.to_ship);

        // Set an OnClickListener to the ImageView
        to_ship_btn.setOnClickListener(v -> {
            // Start the Profile_Settings_Purchase activity when clicked
            startActivity(new Intent(getActivity(), Profile_User_Ship.class));
        });

        //Linked the to receive symbol to purchase
        Button to_receive_btn = view.findViewById(R.id.to_receive);

        // Set an OnClickListener to the ImageView
        to_receive_btn.setOnClickListener(v -> {
            // Start the Profile_Settings_Purchase activity when clicked
            startActivity(new Intent(getActivity(), Profile_User_To_Receive.class));
        });

        //Linked the RECEIVED symbol to purchase
        Button received_btn = view.findViewById(R.id.to_review);

        // Set an OnClickListener to the ImageView
        received_btn.setOnClickListener(v -> {
            // Start the Profile_Settings_Purchase activity when clicked
            startActivity(new Intent(getActivity(), Profile_User_Received.class));
        });

        TextView shop_btn = view.findViewById(R.id.my_shop_profile_user);
        shop_btn.setOnClickListener(v -> {
            // Start the Profile_User_Received activity when clicked
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
        rotate.setDuration(300);
        rotate.start();
    }
}


