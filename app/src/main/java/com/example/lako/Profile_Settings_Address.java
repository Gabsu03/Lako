package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.util.AddressAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Profile_Settings_Address extends AppCompatActivity {

    private List<Profile_Settings_Add_Address.Address> addressList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_address);

        recyclerView = findViewById(R.id.address_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addressAdapter = new AddressAdapter(addressList);
        recyclerView.setAdapter(addressAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Address-User").child(user.getUid()).child("Address");

        loadAddresses();
    }

    // Load addresses from Firebase
    private void loadAddresses() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Profile_Settings_Add_Address.Address address = snapshot.getValue(Profile_Settings_Add_Address.Address.class);
                if (address != null) {
                    address.id = snapshot.getKey(); // Set the Firebase key as ID
                    addressList.add(address);
                    addressAdapter.notifyItemInserted(addressList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle changes if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Profile_Settings_Add_Address.Address removedAddress = snapshot.getValue(Profile_Settings_Add_Address.Address.class);
                if (removedAddress != null) {
                    int index = addressList.indexOf(removedAddress);
                    if (index != -1) {
                        addressList.remove(index);
                        addressAdapter.notifyItemRemoved(index);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Optional: Handle move events if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_Settings_Address.this, "Failed to load addresses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Back button for navigation
    public void address_back(View view) {
        finish(); // Go back to previous activity
    }

    // Add address button
    public void add_address_btn(View view) {
        Intent intent = new Intent(Profile_Settings_Address.this, Profile_Settings_Add_Address.class);
        startActivityForResult(intent, 1); // Request code 1
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Handle new address addition logic (same as before)
            String name = data.getStringExtra("name");
            String label = data.getStringExtra("label");
            String phone = data.getStringExtra("phone");
            String region = data.getStringExtra("region");
            String houseNumber = data.getStringExtra("houseNumber");
            String street = data.getStringExtra("street");
            String city = data.getStringExtra("city");

            Profile_Settings_Add_Address.Address newAddress = new Profile_Settings_Add_Address.Address(
                    name, label, phone, region, houseNumber, street, city
            );

            String key = databaseReference.push().getKey();
            if (key != null) {
                newAddress.id = key;
                databaseReference.child(key).setValue(newAddress)
                        .addOnSuccessListener(unused -> Toast.makeText(this, "Address added successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to add address: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
    }
}


