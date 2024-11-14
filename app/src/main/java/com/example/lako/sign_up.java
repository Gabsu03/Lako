package com.example.lako;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {

    // AUTHENTICATION
    TextInputEditText editTextname, editTextemail, editTextPassword, editTextRetype_pass;
    Button buttonReg;
    CheckBox termsCheckbox;  // Declare the checkbox variable

    FirebaseAuth eAuth;

    // Declare variables to store input data
    private String name, email, password, retype_pass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize the views
        editTextname = findViewById(R.id.name_signup);
        editTextemail = findViewById(R.id.email_signup);
        editTextPassword = findViewById(R.id.password_signup);
        editTextRetype_pass = findViewById(R.id.retype_pass_signup);
        buttonReg = findViewById(R.id.Signup_btn);
        termsCheckbox = findViewById(R.id.terms_checkbox);  // Initialize the checkbox

        eAuth = FirebaseAuth.getInstance();

        if (eAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), sign_in.class));
            finish();
        }

        // Load saved form data when activity is created
        loadFormData();

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                name = editTextname.getText().toString().trim();
                email = editTextemail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                retype_pass = editTextRetype_pass.getText().toString().trim();

                // Check if the user accepted the terms
                if (!termsCheckbox.isChecked()) {
                    Toast.makeText(sign_up.this, "You must accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
                    return; // Prevent further processing if not accepted
                }

                // Validate other fields (same as before)
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(sign_up.this, "Name is Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(sign_up.this, "Email is Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(sign_up.this, "Password is Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(retype_pass)) {
                    Toast.makeText(sign_up.this, "Re-type the Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(sign_up.this, "Required Strong Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                String passwordPattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+";
                Pattern pattern = Pattern.compile(passwordPattern);
                Matcher matcher = pattern.matcher(password);

                if (!matcher.matches()) {
                    Toast.makeText(sign_up.this, "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 numeric digit, and 1 special character", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Instead of registering directly, proceed to the security questions
                Intent intent = new Intent(sign_up.this, Security_Question.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("password", password);  // Pass the password to security questions
                startActivity(intent);
            }
        });
    }

    // Load form data from SharedPreferences when the activity is created
    private void loadFormData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SignUpPrefs", MODE_PRIVATE);

        // Restore data from SharedPreferences
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        String retype_pass = sharedPreferences.getString("retype_pass", "");
        boolean termsChecked = sharedPreferences.getBoolean("termsChecked", false);

        // Set the values to the fields
        editTextname.setText(name);
        editTextemail.setText(email);
        editTextPassword.setText(password);
        editTextRetype_pass.setText(retype_pass);
        termsCheckbox.setChecked(termsChecked);
    }

    // Save instance state (form data) before activity is destroyed (e.g., when navigating away to another activity)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save user input to outState
        outState.putString("name", editTextname.getText().toString());
        outState.putString("email", editTextemail.getText().toString());
        outState.putString("password", editTextPassword.getText().toString());
        outState.putString("retype_pass", editTextRetype_pass.getText().toString());
    }

    // Back button Sign Up
    public void sign_up_back(View view) {
        startActivity(new Intent(sign_up.this, Logo_Page_Activity2.class));
    }

    public void Login(View view) {
        startActivity(new Intent(sign_up.this, sign_in.class));
    }

    public void terms(View view) {
        startActivity(new Intent(sign_up.this, Terms_Conditions.class));
    }
}




