package com.example.lako.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.Main_Shop_Seller_View_Order;
import com.example.lako.R;
import com.example.lako.util.SellerCartItem;

import java.util.List;

public class SellerOrdersAdapter extends RecyclerView.Adapter<SellerOrdersAdapter.OrderViewHolder> {

    private final Context context;
    private final List<SellerCartItem> ordersList;

    public SellerOrdersAdapter(Context context, List<SellerCartItem> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_orders_seller_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        SellerCartItem item = ordersList.get(position);

        holder.productName.setText(item.getProductName());
        holder.quantity.setText("Qty: " + item.getQuantity());
        holder.price.setText("₱" + item.getPrice());

        Glide.with(context)
                .load(item.getProductImage())
                .placeholder(R.drawable.image_upload)
                .into(holder.productImage);

        // Set onClickListener for navigation
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Main_Shop_Seller_View_Order.class);

            // Pass all item details
            intent.putExtra("productId", item.getProductId());
            intent.putExtra("productName", item.getProductName());
            intent.putExtra("productImage", item.getProductImage());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("quantity", item.getQuantity());
            intent.putExtra("buyerId", item.getBuyerId());
            intent.putExtra("status", item.getStatus());

            // Pass address details
            intent.putExtra("addressLabel", item.getAddressLabel());
            intent.putExtra("addressName", item.getAddressName());
            intent.putExtra("addressPhone", item.getAddressPhone());
            intent.putExtra("fullAddress", item.getFullAddress());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView productName, quantity, price;
        ImageView productImage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name_product_list_order);
            quantity = itemView.findViewById(R.id.quantity_amount_product_list_order);
            price = itemView.findViewById(R.id.price_product_list_order);
            productImage = itemView.findViewById(R.id.picture_product_list_order);
        }
    }
}




