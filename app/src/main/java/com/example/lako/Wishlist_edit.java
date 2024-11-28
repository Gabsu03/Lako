package com.example.lako;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Wishlist_edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_edit);
    }

    // This method is called when the back icon in the layout is clicked
    public void edit_wish_icon(View view) {
        // Finish the current activity and go back to the previous activity (WishList)
        finish();
    }

    // Override the onBackPressed method to handle the device's physical back button
    @Override
    public void onBackPressed() {
        // Finish the current activity and go back to the previous activity (WishList)
        finish();
    }
}