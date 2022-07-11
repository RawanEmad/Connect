package com.example.connect.firebase.network;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface FirebaseMessagingService {

    @POST("send")
    Call<NotificationResponse> sendRemoteMessage(
            @HeaderMap HashMap<String,String> headerMap,
            @Body JsonObject remoteBody
    );
}
