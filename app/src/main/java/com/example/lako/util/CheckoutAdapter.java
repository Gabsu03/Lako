package com.example.lako.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.R;

import java.util.Collections;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private final Context context;
    private final List<CartItem> checkoutItems;

    public CheckoutAdapter(Context context, List<CartItem> checkoutItems) {
        this.context = context;
        this.checkoutItems = checkoutItems != null ? checkoutItems : Collections.emptyList(); // Ensure non-null list
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem item = checkoutItems.get(position);
        if (item == null) return; // Prevent NullPointerException

        holder.nameProductCheckout.setText(item.getName() != null ? item.getName() : "No Name");
        holder.nameSellerCheckout.setText(item.getSellerId() != null ? item.getSellerId() : "No Seller");

        Glide.with(context)
                .load(item.getImage() != null ? item.getImage() : R.drawable.image_upload)
                .placeholder(R.drawable.image_upload)
                .into(holder.pictureProductCheckout);

        int totalQuantity = item.getQuantity();
        double unitPrice = 0.0;

        try {
            unitPrice = Double.parseDouble(item.getPrice()); // Safeguard against invalid price format
        } catch (NumberFormatException e) {
            unitPrice = 0.0; // Default to zero if parsing fails
        }

        double totalPrice = totalQuantity * unitPrice;

        holder.quantityCheckout.setText("Qty: " + totalQuantity);
        holder.priceCheckout.setText(String.format("₱%.2f", totalPrice));
    }

    @Override
    public int getItemCount() {
        return checkoutItems.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        TextView nameProductCheckout, nameSellerCheckout, quantityCheckout, priceCheckout;
        ImageView pictureProductCheckout;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProductCheckout = itemView.findViewById(R.id.name_product_checkout);
            nameSellerCheckout = itemView.findViewById(R.id.name_seller_checkout);
            pictureProductCheckout = itemView.findViewById(R.id.picture_product_checkout);
            quantityCheckout = itemView.findViewById(R.id.quantity_checkout);
            priceCheckout = itemView.findViewById(R.id.price_checkout);
        }
    }
}
