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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {

    TextInputEditText editTextname, editTextemail, editTextPassword, editTextRetype_pass;
    Button buttonReg;
    CheckBox termsCheckbox;

    FirebaseAuth eAuth;

    private static final String PREFS_NAME = "SignUpPrefs";

    private String name, email, password, retype_pass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        editTextname = findViewById(R.id.name_signup);
        editTextemail = findViewById(R.id.email_signup);
        editTextPassword = findViewById(R.id.password_signup);
        editTextRetype_pass = findViewById(R.id.retype_pass_signup);
        buttonReg = findViewById(R.id.Signup_btn);
        termsCheckbox = findViewById(R.id.terms_checkbox);

        eAuth = FirebaseAuth.getInstance();

        // Restore form data if exists
        restoreFormData();

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextname.getText().toString().trim();
                email = editTextemail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                retype_pass = editTextRetype_pass.getText().toString().trim();

                if (!termsCheckbox.isChecked()) {
                    Toast.makeText(sign_up.this, "You must accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

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

                if (!password.equals(retype_pass)) {
                    Toast.makeText(sign_up.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(sign_up.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                String passwordPattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+";
                Pattern pattern = Pattern.compile(passwordPattern);
                Matcher matcher = pattern.matcher(password);

                if (!matcher.matches()) {
                    Toast.makeText(sign_up.this, "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 numeric digit, and 1 special character", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save data to SharedPreferences before proceeding
                saveFormData();

                // Create Firebase account
                eAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(sign_up.this, "Account created successfully!", Toast.LENGTH_SHORT).show();


                            // Clear form data
                            clearFormData();

                            // Redirect to Security Question class
                            Intent intent = new Intent(sign_up.this, Security_Question.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(sign_up.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void saveFormData() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", editTextname.getText().toString());
        editor.putString("email", editTextemail.getText().toString());
        editor.putString("password", editTextPassword.getText().toString());
        editor.putString("retype_pass", editTextRetype_pass.getText().toString());
        editor.putBoolean("termsChecked", termsCheckbox.isChecked());
        editor.apply();
    }

    private void restoreFormData() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (preferences.contains("name")) {
            editTextname.setText(preferences.getString("name", ""));
            editTextemail.setText(preferences.getString("email", ""));
            editTextPassword.setText(preferences.getString("password", ""));
            editTextRetype_pass.setText(preferences.getString("retype_pass", ""));
            termsCheckbox.setChecked(preferences.getBoolean("termsChecked", false));
        }
    }

    private void clearFormData() {
        editTextname.setText("");
        editTextemail.setText("");
        editTextPassword.setText("");
        editTextRetype_pass.setText("");
        termsCheckbox.setChecked(false);

        // Clear SharedPreferences data
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreFormData();
    }

    public void Login(View view) {
        clearFormData(); // Clear data before navigating to Login
        startActivity(new Intent(sign_up.this, sign_in.class));
    }

    public void terms(View view) {
        saveFormData(); // Save data before navigating to Terms and Conditions
        startActivity(new Intent(sign_up.this, Terms_Conditions.class));
    }

    public void sign_up_back(View view) {
        clearFormData(); // Clear data before navigating back
        startActivity(new Intent(sign_up.this, Logo_Page_Activity2.class));
    }
}