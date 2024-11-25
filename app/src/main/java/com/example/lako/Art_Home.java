package com.example.lako;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.lako.Fragments.Home;
import com.example.lako.Fragments.Message;
import com.example.lako.Fragments.Notifications;
import com.example.lako.Fragments.Profile_User;
import com.example.lako.Fragments.WishList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Art_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_art_home);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_Home);
        bottomNav.setOnItemSelectedListener(navListener);

        Fragment selectedFragment = new Home();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

    }

    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId(); /* obtain the selected item ID from your source */
                Fragment selectedFragment = null;

                if (itemId == R.id.nav_Wishlist) {
                    selectedFragment = new WishList();
                } else if (itemId == R.id.nav_Notification) {
                    selectedFragment = new Notifications();
                } else if (itemId == R.id.nav_Home) {
                    selectedFragment = new Home();
                } else if (itemId == R.id.nav_Message) {
                    selectedFragment = new Message();
                } else if (itemId == R.id.nav_Profile) {
                    selectedFragment = new Profile_User();

                } else {
                    selectedFragment = new Home();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };
}