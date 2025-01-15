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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        holder.sellerName.setText(item.getSellerId() != null && !item.getSellerId().isEmpty() ? "Shop: " + item.getSellerId() : "Unknown Seller");
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

        // Handle Cancel Order Button Click
        holder.cancelOrderButton.setOnClickListener(v -> cancelOrder(item, position));
    }

    private void cancelOrder(CartItem item, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(context, "Please log in to cancel an order.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders")
                .child(user.getUid()).child(item.getProductId()); // Remove specific order item

        DatabaseReference buyerOrderRef = FirebaseDatabase.getInstance().getReference("BuyerOrders")
                .child(item.getSellerId()).child(user.getUid()).child(item.getProductId()); // Remove buyer's order

        DatabaseReference userOrderRef = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()); // Remove from user's orders

        orderRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                buyerOrderRef.removeValue().addOnCompleteListener(buyerTask -> {
                    if (buyerTask.isSuccessful()) {
                        userOrderRef.removeValue().addOnCompleteListener(userTask -> {
                            if (userTask.isSuccessful()) {
                                Toast.makeText(context, "Order permanently removed.", Toast.LENGTH_SHORT).show();
                                purchaseList.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "Failed to remove user's order.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, "Failed to remove buyer's order.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Failed to remove the order.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public static class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView productName, sellerName, quantity, price;
        ImageView productImage;
        Button cancelOrderButton;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name_product_to_pay);
            sellerName = itemView.findViewById(R.id.name_seller_purchase_to_pay);
            productImage = itemView.findViewById(R.id.picture_product_to_pay);
            quantity = itemView.findViewById(R.id.quantity_amount_product_to_pay);
            price = itemView.findViewById(R.id.price_product_to_pay);
            cancelOrderButton = itemView.findViewById(R.id.cancel_order_to_pay);
        }
    }
}
