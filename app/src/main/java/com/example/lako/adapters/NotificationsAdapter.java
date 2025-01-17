package com.example.lako.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Map;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Map<String, Object>> notificationsList;

    public NotificationsAdapter(Context context, ArrayList<Map<String, Object>> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notifications_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> notification = notificationsList.get(position);

        holder.productNameTextView.setText((String) notification.get("productName"));
        holder.statusTextView.setText((String) notification.get("status"));

        Glide.with(context)
                .load((String) notification.get("productImage"))
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView, statusTextView;
        ShapeableImageView productImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.status_product);
            statusTextView = itemView.findViewById(R.id.date_product);
            productImageView = itemView.findViewById(R.id.product_image);
        }
    }
}

