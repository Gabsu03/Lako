package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Security_Question extends AppCompatActivity {

    private EditText securityAnswer1, securityAnswer2, securityAnswer3, securityAnswer4, securityAnswer5, securityAnswer6, securityAnswer7;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_security_question);

        // Initialize the EditText fields and button
        securityAnswer1 = findViewById(R.id.security_question1);
        securityAnswer2 = findViewById(R.id.security_question2);
        securityAnswer3 = findViewById(R.id.security_question3);
        securityAnswer4 = findViewById(R.id.security_question4);
        securityAnswer5 = findViewById(R.id.security_question5);
        securityAnswer6 = findViewById(R.id.security_question6);
        securityAnswer7 = findViewById(R.id.security_question7);
        button = findViewById(R.id.confirm_button_security_sign_up);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the answers from the EditText fields
                String answer1 = securityAnswer1.getText().toString().trim();
                String answer2 = securityAnswer2.getText().toString().trim();
                String answer3 = securityAnswer3.getText().toString().trim();
                String answer4 = securityAnswer4.getText().toString().trim();
                String answer5 = securityAnswer5.getText().toString().trim();
                String answer6 = securityAnswer6.getText().toString().trim();
                String answer7 = securityAnswer7.getText().toString().trim();

                // Validate that all answers are filled in
                if (TextUtils.isEmpty(answer1) || TextUtils.isEmpty(answer2) || TextUtils.isEmpty(answer3) ||
                        TextUtils.isEmpty(answer4) || TextUtils.isEmpty(answer5) || TextUtils.isEmpty(answer6) ||
                        TextUtils.isEmpty(answer7)) {
                    Toast.makeText(Security_Question.this, "Please answer all security questions", Toast.LENGTH_SHORT).show();
                    return; // Don't proceed further if answers are missing
                }

                // Initialize Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Create a map to hold the security question answers
                Map<String, String> securityAnswers = new HashMap<>();
                securityAnswers.put("question1", answer1);
                securityAnswers.put("question2", answer2);
                securityAnswers.put("question3", answer3);
                securityAnswers.put("question4", answer4);
                securityAnswers.put("question5", answer5);
                securityAnswers.put("question6", answer6);
                securityAnswers.put("question7", answer7);

                // Save answers to Firestore under the current user's UID
                db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(securityAnswers)
                        .addOnSuccessListener(aVoid -> {
                            // Successfully saved answers
                            Toast.makeText(Security_Question.this, "Security questions answered successfully!", Toast.LENGTH_SHORT).show();

                            // After success, navigate to the Login Activity
                            Intent intent = new Intent(Security_Question.this, sign_in.class);
                            startActivity(intent);

                            // Finish the current activity so the user can't go back to it
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            Toast.makeText(Security_Question.this, "Error saving answers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    // Back button to sign-up activity
    public void security_back(View view) {
        startActivity(new Intent(Security_Question.this, sign_up.class)); // Navigate back to Sign-Up page
    }
}


