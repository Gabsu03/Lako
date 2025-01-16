package com.example.lako.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.Profile_Settings_Add_Address;
import com.example.lako.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Profile_Settings_Add_Address.Address> addressList;
    private DatabaseReference databaseReference;

    public AddressAdapter(List<Profile_Settings_Add_Address.Address> addressList) {
        this.addressList = addressList;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Address");
        }
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Profile_Settings_Add_Address.Address address = addressList.get(position);

        holder.name.setText(address.getName());
        holder.label.setText(address.getLabel());
        holder.phone.setText(address.getPhone());
        holder.fullAddress.setText(address.getHouseNumber() + ", " + address.getStreet() + ", " + address.getCity() + ", " + address.getRegion());

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> deleteAddress(position, address, v));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    // Delete address from Firebase and local list
    private void deleteAddress(int position, Profile_Settings_Add_Address.Address address, View view) {
        String addressId = address.id; // Get the address ID for deletion

        if (addressId != null) {
            // Remove address from Firebase
            databaseReference.child(addressId).removeValue()
                    .addOnSuccessListener(unused -> {
                        // Remove address from local list and notify adapter
                        addressList.remove(position);
                        notifyItemRemoved(position);
                        // Use itemView's context for Toast
                        Toast.makeText(view.getContext(), "Address deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Use itemView's context for Toast
                        Toast.makeText(view.getContext(), "Failed to delete address", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView name, label, phone, fullAddress;
        Button deleteButton;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            label = itemView.findViewById(R.id.labelTextView);
            phone = itemView.findViewById(R.id.phoneTextView);
            fullAddress = itemView.findViewById(R.id.fullAddressTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton_address);
        }
    }
}




