package com.example.lako.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.lako.Chat_bot;
import com.example.lako.R;
import com.example.lako.sign_in;

public class Message extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_message, container, false);
        Button chat_bot_btn = view.findViewById(R.id.chat_a_bot);
        chat_bot_btn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Chat_bot.class));
        });

        return view;
    }

}