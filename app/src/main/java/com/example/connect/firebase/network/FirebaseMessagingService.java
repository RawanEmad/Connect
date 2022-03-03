package com.example.connect.firebase.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface FirebaseMessagingService {

    @POST("send")
    Call<String> sendRemoteMessage(
            @HeaderMap HashMap<String,String> headerMap,
            @Body String remoteBody
    );
}
