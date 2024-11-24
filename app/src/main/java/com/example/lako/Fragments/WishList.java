package com.example.lako.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.lako.R;
import com.example.lako.Wishlist_edit;

public class WishList extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        // Find the ImageView by its ID
        ImageView editWishIcon = view.findViewById(R.id.edit_wish_icon);

        // Set an OnClickListener to the ImageView
        editWishIcon.setOnClickListener(v -> {
            // Start the Wishlist edit activity when clicked
            startActivity(new Intent(getActivity(), Wishlist_edit.class));
        });

        return view;
    }
}
