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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {

    private final Context context;
    private final List<CartItem> purchaseList;
    private final DatabaseReference purchaseRef;

    public PurchaseAdapter(Context context, List<CartItem> purchaseList) {
        this.context = context;
        this.purchaseList = purchaseList;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.purchaseRef = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid());
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
        holder.sellerName.setText(item.getSellerId() != null ? "Shop: " + item.getSellerId() : "Unknown Seller");
        holder.quantity.setText("Qty: " + item.getQuantity());

        double price = 0.0;
        try {
            price = Double.parseDouble(item.getPrice());
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        holder.price.setText(String.format("₱%.2f", price));

        Glide.with(context)
                .load(item.getImage() != null ? item.getImage() : R.drawable.image_upload)
                .placeholder(R.drawable.image_upload)
                .into(holder.productImage);

        // Cancel button logic
        holder.cancelOrderButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel this order?")
                    .setPositiveButton("Yes", (dialog, which) -> cancelOrder(item, position))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void cancelOrder(CartItem item, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(context, "Please log in to cancel an order.", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = item.getOrderId();
        String firebaseKey = item.getFirebaseKey();

        if (orderId == null || firebaseKey == null) {
            Toast.makeText(context, "Failed to identify the item for deletion.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to the specific item in the database
        DatabaseReference itemRef = FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(user.getUid())
                .child(orderId)
                .child("items")
                .child(firebaseKey);

        // Remove the specific item
        itemRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Order canceled successfully.", Toast.LENGTH_SHORT).show();

                // Remove the item locally and update the UI
                purchaseList.remove(position);
                notifyItemRemoved(position);

                // Check if the order has no more items and delete the order node
                DatabaseReference orderRef = FirebaseDatabase.getInstance()
                        .getReference("Orders")
                        .child(user.getUid())
                        .child(orderId);
                orderRef.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            orderRef.removeValue(); // Remove the entire order if empty
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to check remaining items.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Failed to cancel the order.", Toast.LENGTH_SHORT).show();
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
