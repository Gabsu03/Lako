package com.example.lako.Fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.lako.ADD_TO_CART;
import com.example.lako.Accessories_Home;
import com.example.lako.Art_Home;
import com.example.lako.Clothing_Home;
import com.example.lako.Living_Home;
import com.example.lako.R;

public class Home extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the TextViews by their IDs
        TextView clothing = view.findViewById(R.id.clothing);
        TextView living = view.findViewById(R.id.living);
        TextView accessories = view.findViewById(R.id.accessories);
        TextView art = view.findViewById(R.id.art);

        //TEMPORARYImageView cart_btn = view.findViewById(R.id.cart_btn);cart_btn.setOnClickListener(v -> {
            // Start the CART ACTIVITYstartActivity(new Intent(getActivity(), ADD_TO_CART.class));});

        // Set an OnClickListener for Clothing
        clothing.setOnClickListener(v -> {
            // Start the Clothing Category activity when clicked
            startActivity(new Intent(getActivity(), Clothing_Home.class));
        });

        // Set an OnClickListener for Living
        living.setOnClickListener(v -> {
            // Start the Living Category activity when clicked
            startActivity(new Intent(getActivity(), Living_Home.class));
        });

        // Set an OnClickListener for Accessories
        accessories.setOnClickListener(v -> {
            // Start the Accessories Category activity when clicked
            startActivity(new Intent(getActivity(), Accessories_Home.class));
        });

        // Set an OnClickListener for Art
        art.setOnClickListener(v -> {
            // Start the Art Category activity when clicked
            startActivity(new Intent(getActivity(), Art_Home.class));
        });

        return view;
    }
}