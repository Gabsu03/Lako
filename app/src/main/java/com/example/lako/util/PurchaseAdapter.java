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

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {

    private final Context context;
    private final List<CartItem> purchaseList;

    public PurchaseAdapter(Context context, List<CartItem> purchaseList) {
        this.context = context;
        this.purchaseList = purchaseList;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.purchase_to_pay_item, parent, false);
        return new PurchaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        CartItem item = purchaseList.get(position);

        holder.productName.setText(item.getName() != null ? item.getName() : "No Name");

        // Ensure Seller Name is Displayed Properly
        if (item.getSellerId() != null && !item.getSellerId().isEmpty()) {
            holder.sellerName.setText("Sold by: " + item.getSellerId());
        } else {
            holder.sellerName.setText("Unknown Seller");  // Default Value if Missing
        }

        holder.quantity.setText("Qty: " + item.getQuantity());

        double price = 0.0;
        try {
            price = Double.parseDouble(item.getPrice());
        } catch (NumberFormatException e) {
            price = 0.0; // Default to zero if price is invalid
        }

        holder.price.setText(String.format("₱%.2f", price));

        Glide.with(context)
                .load(item.getImage() != null && !item.getImage().isEmpty() ? item.getImage() : R.drawable.image_upload)
                .placeholder(R.drawable.image_upload)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public static class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView productName, sellerName, quantity, price;
        ImageView productImage;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name_product_to_pay);
            sellerName = itemView.findViewById(R.id.name_seller_purchase_to_pay);  // Ensure this matches your XML
            productImage = itemView.findViewById(R.id.picture_product_to_pay);
            quantity = itemView.findViewById(R.id.quantity_amount_product_to_pay);
            price = itemView.findViewById(R.id.price_product_to_pay);
        }
    }
}
