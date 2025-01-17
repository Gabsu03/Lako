package com.example.lako.adapters;

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
import com.example.lako.util.CartItem;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private final Context context;
    private final List<CartItem> checkoutItems;

    public CheckoutAdapter(Context context, List<CartItem> checkoutItems) {
        this.context = context;
        this.checkoutItems = checkoutItems;
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

        holder.productName.setText(item.getName());
        holder.sellerName.setText(item.getShopName() != null ? item.getShopName() : "Unknown Shop");
        holder.price.setText(String.format("₱%.2f", Double.parseDouble(item.getPrice()) * item.getQuantity()));
        holder.quantity.setText("Qty: " + item.getQuantity());

        Glide.with(context)
                .load(item.getImage() != null ? item.getImage() : R.drawable.image_upload)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return checkoutItems.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        TextView productName, sellerName, price, quantity;
        ImageView productImage;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name_product_checkout);
            sellerName = itemView.findViewById(R.id.name_seller_checkout);
            price = itemView.findViewById(R.id.price_checkout);
            quantity = itemView.findViewById(R.id.quantity_checkout);
            productImage = itemView.findViewById(R.id.picture_product_checkout);
        }
    }
}