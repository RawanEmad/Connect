package com.example.connect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.connect.R;
import com.example.connect.models.ChatMessages;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private final ArrayList<ChatMessages> chatMessages;
    private final String receivedProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(ArrayList<ChatMessages> chatMessages, String receivedProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receivedProfileImage = receivedProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessages chatMessage = chatMessages.get(position);

        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).getTextMessage().setText(chatMessage.message);
            ((SentMessageViewHolder) holder).getDateTime().setText(chatMessage.dateTime);
        } else {
            ((ReceivedMessageViewHolder) holder).getTextMessage().setText(chatMessage.message);
            ((ReceivedMessageViewHolder) holder).getDateTime().setText(chatMessage.dateTime);
            //Adding Glide library to display images
            Glide.with(mContext)
                    .load(receivedProfileImage)
                    .into(((ReceivedMessageViewHolder) holder).profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage, dateTime;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.sent_message);
            dateTime = itemView.findViewById(R.id.sent_date_time);
        }

        public TextView getTextMessage() {
            return textMessage;
        }

        public TextView getDateTime() {
            return dateTime;
        }
    }

    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage, dateTime;
        CircleImageView profileImage;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.text_message);
            dateTime = itemView.findViewById(R.id.text_date_time);
            profileImage = itemView.findViewById(R.id.received_profile_image);
        }

        public TextView getTextMessage() {
            return textMessage;
        }

        public TextView getDateTime() {
            return dateTime;
        }
    }
}
