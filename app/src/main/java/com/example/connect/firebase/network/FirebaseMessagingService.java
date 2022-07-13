package com.example.connect.firebase.network;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface FirebaseMessagingService {

    @POST("send")
    Call<FirebaseMessagingCall> sendRemoteMessage(
            @HeaderMap HashMap<String,String> headerMap,
            @Body JsonObject remoteBody
    );
}
