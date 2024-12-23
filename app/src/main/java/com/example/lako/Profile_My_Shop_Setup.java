package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_My_Shop_Setup extends AppCompatActivity {

    private EditText shopNameInput, shopDescriptionInput, shopLocationInput;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_shop_setup);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Check if user is logged in
        if (currentUser == null) {
            // Redirect to login if not authenticated
            startActivity(new Intent(this, Main_Shop_Seller_Categories.class));
            finish();
            return;
        }

        // Initialize UI elements
        shopNameInput = findViewById(R.id.shop__name_input);
        shopDescriptionInput = findViewById(R.id.shop__description_input);
        shopLocationInput = findViewById(R.id.shop_location_input);

        // Initialize Firebase Database reference
        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("shops").child(userId);

        // Load existing data from Firebase
        loadDataFromFirebase();
    }

    public void set_up_back(View view) {
        saveDataToFirebase();
        startActivity(new Intent(Profile_My_Shop_Setup.this, Profile_My_Shop_Start.class));
    }

    public void verify_shop_btn(View view) {
        String shopName = shopNameInput.getText().toString().trim();
        String shopDescription = shopDescriptionInput.getText().toString().trim();
        String shopLocation = shopLocationInput.getText().toString().trim();

        if (shopName.isEmpty() || shopDescription.isEmpty() || shopLocation.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save data to Firebase
        saveDataToFirebase();

        // Pass data to the next activity
        Intent intent = new Intent(Profile_My_Shop_Setup.this, Profile_My_Shop_Verify.class);
        intent.putExtra("SHOP_NAME", shopName);
        intent.putExtra("SHOP_DESCRIPTION", shopDescription);
        intent.putExtra("SHOP_LOCATION", shopLocation);
        startActivity(intent);
    }

    private void saveDataToFirebase() {
        String shopName = shopNameInput.getText().toString().trim();
        String shopDescription = shopDescriptionInput.getText().toString().trim();
        String shopLocation = shopLocationInput.getText().toString().trim();

        if (currentUser != null) {
            databaseReference.child("shopName").setValue(shopName);
            databaseReference.child("shopDescription").setValue(shopDescription);
            databaseReference.child("shopLocation").setValue(shopLocation);
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data. User not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    shopNameInput.setText(snapshot.child("shopName").getValue(String.class));
                    shopDescriptionInput.setText(snapshot.child("shopDescription").getValue(String.class));
                    shopLocationInput.setText(snapshot.child("shopLocation").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_My_Shop_Setup.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
