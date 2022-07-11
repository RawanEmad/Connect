package com.example.connect.firebase.network;

import com.example.connect.models.ChatMessages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResponse {

    @SerializedName("to")
    @Expose
    private String receiverToken;

    @SerializedName("data")
    @Expose
    private ChatMessages messageData;

    public NotificationResponse(String receiverToken, ChatMessages messageData) {
        this.receiverToken = receiverToken;
        this.messageData = messageData;
    }

    public String getReceiverToken() {
        return receiverToken;
    }

    public void setReceiverToken(String receiverToken) {
        this.receiverToken = receiverToken;
    }

    public ChatMessages getMessageData() {
        return messageData;
    }

    public void setMessageData(ChatMessages messageData) {
        this.messageData = messageData;
    }
}
