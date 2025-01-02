package com.example.lako.Fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.lako.User_Cart;

public class Home extends Fragment {

    private View view;
    private TextView lastClickedTextView;

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

        // Find the cart button by its ID
        Button cart_btn = view.findViewById(R.id.cart_btn);
        cart_btn.setOnClickListener(v -> {
            // Start the User_cart activity when clicked
            startActivity(new Intent(getActivity(), User_Cart.class));
        });

        // Set OnClickListeners for each TextView
        setTextViewClickListener(clothing, Clothing_Home.class);
        setTextViewClickListener(living, Living_Home.class);
        setTextViewClickListener(accessories, Accessories_Home.class);
        setTextViewClickListener(art, Art_Home.class);

        return view;
    }

    private void setTextViewClickListener(TextView textView, Class<?> targetActivity) {
        textView.setOnClickListener(v -> {
            // Reset the color of the previously clicked TextView (if any)
            if (lastClickedTextView != null) {
                lastClickedTextView.setTextColor(getResources().getColor(R.color.darkbrown, null));
            }

            // Change the color of the clicked TextView
            textView.setTextColor(getResources().getColor(R.color.selected_color, null));

            // Update the reference to the last clicked TextView
            lastClickedTextView = textView;

            // Start the corresponding activity
            startActivity(new Intent(getActivity(), targetActivity));
        });
    }
}



