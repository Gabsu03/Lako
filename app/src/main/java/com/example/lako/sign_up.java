package com.example.lako;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

    FirebaseAuth eAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        editTextname = findViewById(R.id.name_signup);
        editTextemail = findViewById(R.id.email_signup);
        editTextPassword = findViewById(R.id.password_signup);
        editTextRetype_pass = findViewById(R.id.retype_pass_signup);
        buttonReg = findViewById(R.id.Signup_btn);

        eAuth = FirebaseAuth.getInstance();

        if (eAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), sign_in.class));
            finish();
        }

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextname.getText().toString().trim();
                String email = editTextemail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String retype_pass = editTextRetype_pass.getText().toString().trim();

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

                // Register User
                eAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Send email verification
                            FirebaseUser user = eAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Inform the user to verify email
                                                    Toast.makeText(sign_up.this, "Registration Successful! Please verify your email.", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getApplicationContext(), sign_in.class));
                                                } else {
                                                    // Handle failure to send verification email
                                                    Toast.makeText(sign_up.this, "Error sending verification email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(sign_up.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
