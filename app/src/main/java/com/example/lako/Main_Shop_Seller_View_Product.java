
package com.example.lako;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Main_Shop_Seller_View_Product extends AppCompatActivity {

    private ImageView productImageView, menuProduct;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, productSpecificationTextView;

    // Firebase reference
    private DatabaseReference databaseReference;
    private String productId; // You need to pass the product ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_view_product);

        // Initialize the views
        productImageView = findViewById(R.id.picture_of_product);
        menuProduct = findViewById(R.id.menu_product);
        productNameTextView = findViewById(R.id.name_of_product_display);
        productPriceTextView = findViewById(R.id.price);
        productDescriptionTextView = findViewById(R.id.description_product);
        productSpecificationTextView = findViewById(R.id.specification_of_product);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        productId = getIntent().getStringExtra("productId");

        Log.d("PRODUCT_INFO", "Product ID: " + productId);  // Log the product ID to ensure it's passed correctly

        // Get other product details
        String productName = getIntent().getStringExtra("productName");
        String productPrice = getIntent().getStringExtra("productPrice");
        String productImage = getIntent().getStringExtra("productImage");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productSpecification = getIntent().getStringExtra("productSpecification");

        // Set the product data to the views
        if (productName != null && productPrice != null) {
            productNameTextView.setText(productName);
            productPriceTextView.setText("₱" + productPrice);
        }

        if (productImage != null && !productImage.isEmpty()) {
            Glide.with(this)
                    .load(productImage)
                    .placeholder(R.drawable.image_upload)
                    .into(productImageView);
        }

        if (productDescription != null && !productDescription.isEmpty()) {
            productDescriptionTextView.setText(productDescription);
        } else {
            productDescriptionTextView.setText("No description available.");
        }

        if (productSpecification != null && !productSpecification.isEmpty()) {
            productSpecificationTextView.setText(productSpecification);
        } else {
            productSpecificationTextView.setText("No specifications available.");
        }

        // Set up the three-dot menu
        menuProduct.setOnClickListener(v -> showPopupMenu(v));
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        // Inflate the menu
        popupMenu.inflate(R.menu.popup_menu);

        // Show icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupMenu.setForceShowIcon(true);
        }

        // Show the popup menu
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.edit) {
                Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show();
                // Add your edit functionality here
                return true;
            } else if (itemId == R.id.delete) {
                showDeleteConfirmationDialog(); // Show confirmation dialog before deleting
                return true;
            } else {
                return false;
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> deleteProductFromFirebase())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteProductFromFirebase() {
        if (productId != null) {
            Log.d("DELETE_PRODUCT", "Deleting product with ID: " + productId);
            databaseReference.child(productId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();

                        // Send result back to the previous activity
                        Intent intent = new Intent();
                        intent.putExtra("productDeleted", true);
                        setResult(RESULT_OK, intent);
                        finish(); // Go back to the previous activity
                    })
                    .addOnFailureListener(e -> {
                        Log.e("DELETE_PRODUCT", "Error deleting product", e);
                        Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("DELETE_PRODUCT", "Product ID is null");
            Toast.makeText(this, "Product ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

    public void seller_view_product_btn(View view) {
        finish(); // Close the current activity and go back
    }
}




