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
import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private final ArrayList<ChatMessages> chatMessages;
    private final String receivedProfileImage;
    private final String senderId;
    private final String type;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    public static final int VIEW_TYPE_SENT_VOICE = 3;
    public static final int VIEW_TYPE_RECEIVED_VOICE = 4;

    public ChatAdapter(ArrayList<ChatMessages> chatMessages, String receivedProfileImage, String senderId, String type) {
        this.chatMessages = chatMessages;
        this.receivedProfileImage = receivedProfileImage;
        this.senderId = senderId;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVED) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_SENT_VOICE) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_sent_voice, parent, false);
            return new SentVoiceViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_container_received_voice, parent, false);
            return new ReceivedVoiceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessages chatMessage = chatMessages.get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_SENT:
                ((SentMessageViewHolder) holder).getTextMessage().setText(chatMessage.message);
                ((SentMessageViewHolder) holder).getDateTime().setText(chatMessage.dateTime);
                break;
            case VIEW_TYPE_RECEIVED:
                ((ReceivedMessageViewHolder) holder).getTextMessage().setText(chatMessage.message);
                ((ReceivedMessageViewHolder) holder).getDateTime().setText(chatMessage.dateTime);
                //Adding Glide library to display images
                Glide.with(mContext)
                        .load(receivedProfileImage)
                        .into(((ReceivedMessageViewHolder) holder).profileImage);
                break;
            case VIEW_TYPE_SENT_VOICE:
                ((SentVoiceViewHolder) holder).getVoicePlayerView().setAudio(chatMessage.message);
                ((SentVoiceViewHolder) holder).getDateTime().setText(chatMessage.dateTime);
                break;
            case VIEW_TYPE_RECEIVED_VOICE:
                ((ReceivedVoiceViewHolder) holder).getVoicePlayerView().setAudio(chatMessage.message);
                ((ReceivedVoiceViewHolder) holder).getDateTime().setText(chatMessage.dateTime);
                //Adding Glide library to display images
                Glide.with(mContext)
                        .load(receivedProfileImage)
                        .into(((ReceivedVoiceViewHolder) holder).profileImage);

        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
            if (chatMessages.get(position).type.equals("text")){
                return VIEW_TYPE_SENT;
            } else {
                return VIEW_TYPE_SENT_VOICE;
            }
        } else {
            if (chatMessages.get(position).type.equals("text")){
                return VIEW_TYPE_RECEIVED;
            } else {
                return VIEW_TYPE_RECEIVED_VOICE;
            }
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

    public static class SentVoiceViewHolder extends RecyclerView.ViewHolder {

        VoicePlayerView voicePlayerView;
        TextView dateTime;

        public SentVoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            voicePlayerView = itemView.findViewById(R.id.sentVoicePlayerView);
            dateTime = itemView.findViewById(R.id.sent_date_time);
        }

        public VoicePlayerView getVoicePlayerView() {
            return voicePlayerView;
        }

        public TextView getDateTime() {
            return dateTime;
        }
    }

    public static class ReceivedVoiceViewHolder extends RecyclerView.ViewHolder {

        VoicePlayerView voicePlayerView;
        TextView dateTime;
        CircleImageView profileImage;

        public ReceivedVoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            voicePlayerView = itemView.findViewById(R.id.receivedVoicePlayerView);
            dateTime = itemView.findViewById(R.id.text_date_time);
            profileImage = itemView.findViewById(R.id.received_profile_image);
        }

        public VoicePlayerView getVoicePlayerView() {
            return voicePlayerView;
        }

        public TextView getDateTime() {
            return dateTime;
        }
    }
}
