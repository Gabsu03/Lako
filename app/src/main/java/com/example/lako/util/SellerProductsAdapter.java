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

public class SellerProductsAdapter extends RecyclerView.Adapter<SellerProductsAdapter.ProductViewHolder> {
    private Context context;
    private List<Productt> productList;

    public SellerProductsAdapter(Context context, List<Productt> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_products_item_seller, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Productt product = productList.get(position);

        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText("₱" + product.getPrice());

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.image_upload)
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, productPriceTextView;
        ImageView productImageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.Name_of_products);
            productPriceTextView = itemView.findViewById(R.id.product_price_add_item);
            productImageView = itemView.findViewById(R.id.product_imageeee);
        }
    }
}

