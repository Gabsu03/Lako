package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.adapters.EmailAdapter;
import com.example.lako.util.EmailItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile_Settings_Add_MFA extends AppCompatActivity {

    private EditText secondaryEmailInput;
    private Button saveEmailButton;
    private RecyclerView secondaryRecyclerView;
    private EmailAdapter emailAdapter;
    private List<EmailItem> emailList;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings_add_mfa);

        // Initialize views
        secondaryEmailInput = findViewById(R.id.secondary_email_input);
        saveEmailButton = findViewById(R.id.save_email_button);
        secondaryRecyclerView = findViewById(R.id.secondary_view);

        // Initialize RecyclerView
        emailList = new ArrayList<>();
        emailAdapter = new EmailAdapter(emailList);
        secondaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        secondaryRecyclerView.setAdapter(emailAdapter);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please log in to add a secondary email.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Multi-Factor").child(currentUser.getUid());

        // Load existing emails from Firebase
        loadEmails();

        // Save email when button is clicked
        saveEmailButton.setOnClickListener(view -> saveSecondaryEmail());
    }

    private void saveSecondaryEmail() {
        String email = secondaryEmailInput.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailId = databaseReference.push().getKey(); // Generate a unique ID for the email

        if (emailId != null) {
            EmailItem emailItem = new EmailItem(email, false); // Create an email item
            databaseReference.child(emailId).setValue(emailItem)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Email saved successfully.", Toast.LENGTH_SHORT).show();
                            secondaryEmailInput.setText(""); // Clear the input field
                        } else {
                            Toast.makeText(this, "Failed to save email. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void loadEmails() {
        // Attach a listener to the database reference to track real-time changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emailList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot emailSnapshot : snapshot.getChildren()) {
                    EmailItem emailItem = emailSnapshot.getValue(EmailItem.class);
                    if (emailItem != null) {
                        emailList.add(emailItem); // Add each email to the list
                    }
                }
                emailAdapter.notifyDataSetChanged(); // Refresh RecyclerView automatically
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_Settings_Add_MFA.this, "Failed to load emails: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Navigate back to MFA settings
    public void drop_up_mfa(View view) {
        Intent intent = new Intent(Profile_Settings_Add_MFA.this, Profile_Settings_MFA.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}




