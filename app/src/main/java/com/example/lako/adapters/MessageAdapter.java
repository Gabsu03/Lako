package com.example.lako.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lako.R;
import com.example.lako.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messages;
    private String currentUserId;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageText.setText(message.getMessageText());
        holder.messageTime.setText(formatTimestamp(message.getTimestamp(), holder.itemView.getContext()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            messageTime = itemView.findViewById(R.id.message_time);
        }
    }

    private String formatTimestamp(long timestamp, android.content.Context context) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
        return dateFormat.format(new java.util.Date(timestamp));
    }
}
