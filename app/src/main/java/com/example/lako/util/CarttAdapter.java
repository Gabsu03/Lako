package com.example.lako.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarttAdapter extends RecyclerView.Adapter<CarttAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private Set<CartItem> selectedItems = new HashSet<>(); // To track selected items
    private OnItemSelectionChangedListener listener; // Listener to notify selection changes

    public CarttAdapter(Context context, List<CartItem> cartItems, OnItemSelectionChangedListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.cartNameOfProduct.setText(item.getName());
        holder.cartPriceOfProduct.setText("₱" + item.getPrice() + " (each)");
        holder.quantityProduct.setText(item.getQuantity() + "x");
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.image_upload)
                .into(holder.cartImage);

        // Fetch seller name and update TextView
        fetchSellerName(item.getSellerId(), holder.cartNameOfShop);

        // Set the checkbox state based on whether the item is selected
        holder.checkboxCart.setChecked(selectedItems.contains(item));

        // Handle checkbox clicks
        holder.checkboxCart.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }

            // Notify the activity about selection changes
            if (listener != null) {
                listener.onSelectionChanged(calculateTotalPrice());
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public ArrayList<CartItem> getSelectedItems() {
        return new ArrayList<>(selectedItems); // Convert Set to ArrayList and return
    }

    // Select or deselect all items
    public void selectAllItems(boolean isSelected) {
        selectedItems.clear();
        if (isSelected) {
            selectedItems.addAll(cartItems); // Add all items to the selected list
        }
        notifyDataSetChanged(); // Refresh the list to update checkbox states
    }

    // Calculate the total price of selected items
    public double calculateTotalPrice() {
        double total = 0.0;
        for (CartItem item : selectedItems) {
            total += Double.parseDouble(item.getPrice()) * item.getQuantity();
        }
        return total;
    }

    private void fetchSellerName(String sellerId, TextView shopNameTextView) {
        if (sellerId == null || sellerId.isEmpty()) {
            shopNameTextView.setText("Unknown Seller"); // Fallback for missing sellerId
            return;
        }

        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference("sellers").child(sellerId);
        sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String sellerName = snapshot.child("name").getValue(String.class);
                    if (sellerName != null) {
                        shopNameTextView.setText(sellerName); // Set seller's name
                    } else {
                        shopNameTextView.setText("Unknown Seller"); // Fallback for missing name
                    }
                } else {
                    shopNameTextView.setText("Unknown Seller"); // Fallback if seller not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shopNameTextView.setText("Error Fetching Seller"); // Error case
            }
        });
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView cartNameOfProduct, cartPriceOfProduct, cartNameOfShop, quantityProduct;
        CheckBox checkboxCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.image_cart);
            cartNameOfProduct = itemView.findViewById(R.id.cart_name_of_product);
            cartPriceOfProduct = itemView.findViewById(R.id.cart_price_of_product);
            cartNameOfShop = itemView.findViewById(R.id.cart_name_of_shop);
            checkboxCart = itemView.findViewById(R.id.checkbox_cart);
            quantityProduct = itemView.findViewById(R.id.quantity_product);
        }
    }


    // Interface to notify selection changes
    public interface OnItemSelectionChangedListener {
        void onSelectionChanged(double totalPrice);
    }
}





