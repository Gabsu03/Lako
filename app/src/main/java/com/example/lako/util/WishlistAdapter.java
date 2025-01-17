package com.example.lako.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private Context context;
    private List<Productt> productList;

    public WishlistAdapter(Context context, List<Productt> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_items, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Productt product = productList.get(position);

        // Set product name and price
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText("₱" + product.getPrice());

        // Set product image
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.image_upload)
                .into(holder.productImageView);

        // Fetch and display seller name
        fetchSellerName(product.getSellerId(), holder.productNameShopTextView);
    }

    private void fetchSellerName(String sellerId, TextView shopNameTextView) {
        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("sellers").child(sellerId);
        sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String sellerName = snapshot.child("name").getValue(String.class);
                    if (sellerName != null) {
                        shopNameTextView.setText(sellerName);
                    } else {
                        shopNameTextView.setText("Unknown Seller");
                    }
                } else {
                    shopNameTextView.setText("Unknown Seller");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shopNameTextView.setText("Unknown Seller");
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


        public class WishlistViewHolder extends RecyclerView.ViewHolder {
            TextView productNameTextView, productPriceTextView, productNameShopTextView;
            ImageView productImageView;

            public WishlistViewHolder(View itemView) {
                super(itemView);
                productNameTextView = itemView.findViewById(R.id.name_product_wishlist);
                productPriceTextView = itemView.findViewById(R.id.product_price_wishlist);
                productImageView = itemView.findViewById(R.id.picture_product);
                productNameShopTextView = itemView.findViewById(R.id.wishlist_name_shop); // Correct ID
            }
        }


        public void loadWishlistItems() {
        // Get the current user's UID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists").child(userId);

        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();  // Clear existing list to avoid duplicates

                // Check if data is being retrieved correctly
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Productt product = productSnapshot.getValue(Productt.class);
                        if (product != null) {
                            productList.add(product);  // Add the product to the list
                        }
                    }
                    Log.d("WishList", "Loaded " + productList.size() + " products.");
                } else {
                    Log.d("WishList", "No products in wishlist.");
                }

                // Notify adapter to refresh the list
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading wishlist items: " + error.getMessage());
            }
        });
    }
}





