package com.example.lako;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.Fragments.Message;

import java.util.ArrayList;
import java.util.List;

public class Chat_bot extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message_bot> messageList;
    private EditText messageInput;
    private Button sendButton;
    private LinearLayout suggestedQuestionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        // Initialize Views
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        suggestedQuestionsLayout = findViewById(R.id.suggestedQuestions);

        // Initialize Chat
        messageList = new ArrayList<Message_bot>();
        chatAdapter = new ChatAdapter(messageList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Suggested Questions
        String[] suggestedQuestions = {
                "How do I become a seller?",
                "What are the fees for selling?",
                "How can I upload a product?",
                "When will I get paid for a sale?",
                "How do I handle returns?",
                "How do I set the price for my product?",
                "How can I track my sales?",
                "Can I edit my product details after uploading?",
                "What happens if my product gets rejected?",
                "How can I improve my product visibility?",
                "How do I manage my inventory?",
                "Can I offer discounts or vouchers?",
                "How do I know if an order is confirmed?",
                "When will I receive my order?",
                "How can I contact a seller?",
                "What payment methods do you accept?",
                "How can I request a refund or return?",
                "What happens if the seller cancels my order?",
                "Can I leave a review for a product?",
                "How can I update my profile information?",
                "How do I reset my password?",
                "What should I do if I don't receive my payment?",
                "How do I report an issue with an order?"
        };

        // Add buttons for suggested questions
        for (String question : suggestedQuestions) {
            Button questionButton = new Button(this);
            questionButton.setText(question);
            questionButton.setAllCaps(false);
            questionButton.setBackgroundResource(R.drawable.suggested_button_bg); // Custom style
            questionButton.setTextColor(getResources().getColor(android.R.color.black));
            questionButton.setPadding(20, 10, 20, 10);

            // Handle button click
            questionButton.setOnClickListener(v -> {
                addUserMessage(question);
                botResponse(question);
            });

            // Add button to layout
            suggestedQuestionsLayout.addView(questionButton);
        }

        // Send Button Action
        sendButton.setOnClickListener(v -> {
            String userInput = messageInput.getText().toString().trim();
            if (!userInput.isEmpty()) {
                addUserMessage(userInput);
                messageInput.setText("");
                botResponse(userInput);
            }
        });
    }

    public void chat_bot_back(View view) {
        // Navigate to MainActivity, which will ensure Profile_User fragment is shown
        Intent intent = new Intent(Chat_bot.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear all activities above it in the stack
        startActivity(intent);
        finish();
    }

    private void addUserMessage(String message) {
        messageList.add(new Message_bot(message, true));
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void botResponse(String userMessage) {
        String response;

        // Convert user input to lowercase for easier matching
        String lowerCaseMessage = userMessage.toLowerCase();

        if (lowerCaseMessage.contains("become a seller")) {
            response = "To become a seller, simply go to Profile > Become a Seller and follow the steps.";
        } else if (lowerCaseMessage.contains("fees")) {
            response = "We charge a small commission fee per successful sale. You can find the detailed breakdown in Profile > Seller Fees.";
        } else if (lowerCaseMessage.contains("upload a product")) {
            response = "Go to Profile > My Products > Add New Product. Enter the details and upload images.";
        } else if (lowerCaseMessage.contains("get paid")) {
            response = "Payments are processed within 3-5 business days after delivery.";
        } else if (lowerCaseMessage.contains("handle returns")) {
            response = "If a customer requests a return, check Profile > Returns for instructions.";
        } else if (lowerCaseMessage.contains("set the price")) {
            response = "You can set the price of your product during the upload process. Go to My Products > Add Product.";
        } else if (lowerCaseMessage.contains("track my sales")) {
            response = "You can track your sales from the Dashboard in the Seller Profile.";
        } else if (lowerCaseMessage.contains("edit my product details")) {
            response = "To edit product details, go to My Products, select the product, and tap Edit.";
        } else if (lowerCaseMessage.contains("product gets rejected")) {
            response = "If your product gets rejected, review the rejection reason in Notifications > Product Status.";
        } else if (lowerCaseMessage.contains("improve my product visibility")) {
            response = "Improve visibility by using clear images, accurate descriptions, and competitive pricing.";
        } else if (lowerCaseMessage.contains("manage my inventory")) {
            response = "Go to Profile > Inventory Management to update stock levels.";
        } else if (lowerCaseMessage.contains("offer discounts or vouchers")) {
            response = "You can create discounts or vouchers under Promotions in your Seller Profile.";
        } else if (lowerCaseMessage.contains("order is confirmed")) {
            response = "You’ll receive a confirmation notification in your Dashboard once the order is confirmed.";
        } else if (lowerCaseMessage.contains("receive my order")) {
            response = "Delivery times vary based on the seller’s location. Check order details for estimated delivery.";
        } else if (lowerCaseMessage.contains("contact a seller")) {
            response = "You can contact a seller via the Chat option on the product page.";
        } else if (lowerCaseMessage.contains("payment methods")) {
            response = "We accept credit/debit cards, PayPal, and bank transfers.";
        } else if (lowerCaseMessage.contains("request a refund or return")) {
            response = "Go to Orders > Request Refund/Return and follow the instructions.";
        } else if (lowerCaseMessage.contains("seller cancels my order")) {
            response = "If a seller cancels your order, you’ll receive a notification and a refund will be processed.";
        } else if (lowerCaseMessage.contains("leave a review")) {
            response = "After receiving your product, go to Orders > Leave Review.";
        } else if (lowerCaseMessage.contains("update my profile information")) {
            response = "Go to Profile > Edit Profile to update your details.";
        } else if (lowerCaseMessage.contains("reset my password")) {
            response = "You can reset your password by tapping Forgot Password on the login screen.";
        } else if (lowerCaseMessage.contains("don't receive my payment")) {
            response = "If you don’t receive your payment, contact Support from Profile > Help Center.";
        } else if (lowerCaseMessage.contains("report an issue")) {
            response = "You can report an issue with an order by selecting it in Orders > Report Issue.";
        } else {
            response = "I'm here to help! Please try rephrasing your question if you don't see an answer.";
        }

        // Add bot message to chat
        messageList.add(new Message_bot(response, false));
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }
}