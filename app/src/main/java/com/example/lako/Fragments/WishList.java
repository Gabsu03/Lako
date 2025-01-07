package com.example.lako.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.R;
import com.example.lako.util.Productt;
import com.example.lako.util.WishlistAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishList extends Fragment {

    private RecyclerView recyclerView;
    private WishlistAdapter adapter;
    private List<Productt> productList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.wishlistRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WishlistAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);

        // Load wishlist items
        adapter.loadWishlistItems();

        // Register the broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateWishlistReceiver, new IntentFilter("com.yourapp.UPDATE_WISHLIST"));

        return view;
    }

    // BroadcastReceiver to update the wishlist
    private final BroadcastReceiver updateWishlistReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Refresh the wishlist items when the broadcast is received
            adapter.loadWishlistItems();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the receiver when the fragment is destroyed to avoid memory leaks
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateWishlistReceiver);
    }
}




