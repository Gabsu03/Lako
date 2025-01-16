package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Settings_Add_Address extends AppCompatActivity {

    private TextInputEditText nameInput, labelInput, phoneInput, regionInput, houseNumberInput, streetInput, cityInput;
    private Button saveButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_add_address); // Set the correct layout

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        labelInput = findViewById(R.id.labelInput);
        phoneInput = findViewById(R.id.phoneInput);
        regionInput = findViewById(R.id.regionInput);
        houseNumberInput = findViewById(R.id.houseNumberInput);
        streetInput = findViewById(R.id.streetInput);
        cityInput = findViewById(R.id.cityInput);
        saveButton = findViewById(R.id.address_save_btn); // Get reference for the Save button

        // Save address to Firebase when the Save button is clicked
        saveButton.setOnClickListener(view -> saveAddress());
    }

    private void saveAddress() {
        // Retrieve data from input fields
        String name = nameInput.getText().toString().trim();
        String label = labelInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String region = regionInput.getText().toString().trim();
        String houseNumber = houseNumberInput.getText().toString().trim();
        String street = streetInput.getText().toString().trim();
        String city = cityInput.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(label) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(region) || TextUtils.isEmpty(houseNumber) || TextUtils.isEmpty(street) || TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Address object
        Address address = new Address(name, label, phone, region, houseNumber, street, city);

        // Get current user ID (make sure the user is authenticated)
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique address ID for this address entry
        String addressId = databaseReference.child(userId).child("Address").push().getKey();
        if (addressId != null) {
            // Save the address to Firebase under the generated address ID
            databaseReference.child(userId).child("Address").child(addressId).setValue(address)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Address saved successfully", Toast.LENGTH_SHORT).show();

                        // Return the saved address data back to Profile_Settings_Address activity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("name", name);
                        resultIntent.putExtra("label", label);
                        resultIntent.putExtra("phone", phone);
                        resultIntent.putExtra("region", region);
                        resultIntent.putExtra("houseNumber", houseNumber);
                        resultIntent.putExtra("street", street);
                        resultIntent.putExtra("city", city);

                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Failed to generate address ID", Toast.LENGTH_SHORT).show();
        }
    }

    public static class Address {
        private String name;
        private String label;
        private String phone;
        private String region;
        private String houseNumber;
        private String street;
        private String city;
        public String id; // Optional, for storing a Firebase-generated key if needed

        // Default constructor required for Firebase
        public Address() {}

        // Constructor for initializing the fields
        public Address(String name, String label, String phone, String region, String houseNumber, String street, String city) {
            this.name = name;
            this.label = label;
            this.phone = phone;
            this.region = region;
            this.houseNumber = houseNumber;
            this.street = street;
            this.city = city;
        }

        // Getters for all fields
        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public String getPhone() {
            return phone;
        }

        public String getRegion() {
            return region;
        }

        public String getHouseNumber() {
            return houseNumber;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }

        // Optional: Setter methods, if you need to modify the fields after object creation
        public void setName(String name) {
            this.name = name;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

}
