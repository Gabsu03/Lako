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
import com.example.lako.util.Productt;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Productt> cartItemList;
    private Context context;

    public CartAdapter(List<Productt> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_products_item_seller, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Productt product = cartItemList.get(position);

        holder.priceCart.setText("₱" + product.getPrice());
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.image_upload)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView priceCart;
        ImageView productImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            priceCart = itemView.findViewById(R.id.price_cart);
            productImage = itemView.findViewById(R.id.image_product_cart);
        }
    }
}

