package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main_Shop_Seller_Edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_shop_seller_edit);

    }

    //TEMPORARY

    public void save_button_edit_profile_seller(View view) {
        startActivity(new Intent(Main_Shop_Seller_Edit.this, Main_Shop_Seller_Edit_Profile_Loading_Screen.class));
    }
}