package com.example.lako.util;

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
import com.example.lako.Main_Shop_Seller_View_Product;
import com.example.lako.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_products_item_seller, parent, false);
        context = parent.getContext(); // Save the context to use later
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText("₱" + product.getPrice());

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(holder.productImage.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.image_upload)
                    .into(holder.productImage);
        }

        // Set a click listener on the item
        holder.itemView.setOnClickListener(v -> {
            // Pass product data to the next activity
            Intent intent = new Intent(context, Main_Shop_Seller_View_Product.class);
            intent.putExtra("productName", product.getName());
            intent.putExtra("productPrice", product.getPrice());
            intent.putExtra("productImage", product.getImage());
            intent.putExtra("productDescription", product.getDescription()); // Pass description
            intent.putExtra("productSpecification", product.getSpecification()); // Pass specification
            context.startActivity(intent);
        });
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
            productImage = itemView.findViewById(R.id.product_imageeee);
        }
    }
}
