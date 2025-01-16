package com.example.lako.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SellerOrdersAdapter extends RecyclerView.Adapter<SellerOrdersAdapter.SellerOrderViewHolder> {

    private final Context context;
    private final List<CartItem> ordersList;

    public SellerOrdersAdapter(Context context, List<CartItem> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public SellerOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_orders_seller_item, parent, false);
        return new SellerOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerOrderViewHolder holder, int position) {
        CartItem item = ordersList.get(position);

        // Display product name
        holder.productName.setText(item.getName() != null ? item.getName() : "No Name");

        // Display quantity
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        // Display price
        holder.price.setText(String.format("₱%.2f", Double.parseDouble(item.getPrice())));

        // Display image
        Glide.with(context)
                .load(item.getImage() != null && !item.getImage().isEmpty() ? item.getImage() : R.drawable.image_upload)
                .placeholder(R.drawable.image_upload)
                .into(holder.productImage);

        // Ship Button logic
        holder.shipButton.setOnClickListener(v -> {
            DatabaseReference orderRef = FirebaseDatabase.getInstance()
                    .getReference("Orders")
                    .child(item.getUserId())
                    .child(item.getOrderId()); // Assuming `orderId` is a field in CartItem

            orderRef.child("status").setValue("Shipped").addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Order marked as shipped.", Toast.LENGTH_SHORT).show();
                    ordersList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Failed to update status.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class SellerOrderViewHolder extends RecyclerView.ViewHolder {
        TextView productName, quantity, price;
        ImageView productImage;
        Button shipButton;

        public SellerOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name_product_list_order);
            quantity = itemView.findViewById(R.id.quantity_amount_product_list_order);
            price = itemView.findViewById(R.id.price_product_list_order);
            productImage = itemView.findViewById(R.id.picture_product_list_order);
            shipButton = itemView.findViewById(R.id.ship_btn);
        }
    }
}
