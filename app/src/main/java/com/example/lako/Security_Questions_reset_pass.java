package com.example.lako;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class Security_Questions_reset_pass extends AppCompatActivity {

    private EditText securityAnswer1, securityAnswer2, securityAnswer3, securityAnswer4, securityAnswer5, securityAnswer6, securityAnswer7;
    private Button button;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_security_questions_reset_pass);

        // Initialize Firestore and Firebase Auth
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize EditText fields and button
        securityAnswer1 = findViewById(R.id.reset_security_question1);
        securityAnswer2 = findViewById(R.id.reset_security_question2);
        securityAnswer3 = findViewById(R.id.reset_security_question3);
        securityAnswer4 = findViewById(R.id.reset_security_question4);
        securityAnswer5 = findViewById(R.id.reset_security_question5);
        securityAnswer6 = findViewById(R.id.reset_security_question6);
        securityAnswer7 = findViewById(R.id.reset_security_question7);
        button = findViewById(R.id.confirm_button_security_sign_up);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySecurityAnswers();
            }
        });
    }

    private void verifySecurityAnswers() {
        // Retrieve answers from the input fields
        String answer1 = securityAnswer1.getText().toString().trim();
        String answer2 = securityAnswer2.getText().toString().trim();
        String answer3 = securityAnswer3.getText().toString().trim();
        String answer4 = securityAnswer4.getText().toString().trim();
        String answer5 = securityAnswer5.getText().toString().trim();
        String answer6 = securityAnswer6.getText().toString().trim();
        String answer7 = securityAnswer7.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(answer1) || TextUtils.isEmpty(answer2) || TextUtils.isEmpty(answer3) ||
                TextUtils.isEmpty(answer4) || TextUtils.isEmpty(answer5) || TextUtils.isEmpty(answer6) ||
                TextUtils.isEmpty(answer7)) {
            Toast.makeText(Security_Questions_reset_pass.this, "Please answer all security questions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current user's UID or email (if using Forgot Password, user may enter email first)
        String email = getIntent().getStringExtra("email");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Email not provided. Cannot verify user.", Toast.LENGTH_SHORT).show();
            Log.e("SecurityQuestions", "Email was not provided in intent.");
            return;
        }

        Log.d("SecurityQuestions", "Email received: " + email);

        // Query Firestore for the user's security answers
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // Retrieve stored security answers
                        Map<String, Object> securityAnswers = (Map<String, Object>) document.get("security_answers");

                        if (securityAnswers == null) {
                            Toast.makeText(Security_Questions_reset_pass.this, "Security answers not found for this account.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Compare input answers with stored answers
                        boolean allMatch = Objects.equals(securityAnswers.get("question1"), answer1) &&
                                Objects.equals(securityAnswers.get("question2"), answer2) &&
                                Objects.equals(securityAnswers.get("question3"), answer3) &&
                                Objects.equals(securityAnswers.get("question4"), answer4) &&
                                Objects.equals(securityAnswers.get("question5"), answer5) &&
                                Objects.equals(securityAnswers.get("question6"), answer6) &&
                                Objects.equals(securityAnswers.get("question7"), answer7);

                        if (allMatch) {
                            // Answers are correct, navigate to Reset Password screen
                            Intent intent = new Intent(Security_Questions_reset_pass.this, Password_Reset.class);
                            intent.putExtra("email", email); // Pass email to the Reset_Password activity
                            startActivity(intent);
                            finish();
                        } else {
                            // Answers do not match
                            Toast.makeText(Security_Questions_reset_pass.this, "Security answers are incorrect. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Security_Questions_reset_pass.this, "No account found with the provided email.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Security_Questions_reset_pass.this, "Error retrieving security answers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}