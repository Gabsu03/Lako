package com.example.lako.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lako.Main_Shop_Seller_View_Product;
import com.example.lako.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private boolean isSellerView; // Flag to differentiate between seller and user views

    // Constructor to accept the product list and the view type
    public ProductAdapter(List<Product> productList, boolean isSellerView) {
        this.productList = productList;
        this.isSellerView = isSellerView;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isSellerView ? R.layout.display_products_item_seller : R.layout.display_product_user_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext(); // Save context
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText("₱" + product.getPrice());

        // Loading image with Glide
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(holder.productImage.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.image_upload)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.image_upload); // Default image if no URL
        }

        // Seller view: Navigate to product editing/viewing screen
        if (isSellerView) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, Main_Shop_Seller_View_Product.class);
                intent.putExtra("productId", product.getId()); // Pass product ID to the next activity
                intent.putExtra("productName", product.getName());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("productImage", product.getImage());
                intent.putExtra("productDescription", product.getDescription());
                intent.putExtra("productSpecification", product.getSpecification());
                context.startActivity(intent);
            });
        }
        // User view: Show product details or add to cart
        else {
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Product: " + product.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ShapeableImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.Name_of_products);
            productPrice = itemView.findViewById(R.id.product_price_add_item);
            productImage = itemView.findViewById(R.id.product_image_home);
        }
    }
}

