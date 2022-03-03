package com.example.connect.firebase.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseMessagingClient {

    public static final String BASE_URL = "https://fcm.googleapis.com/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder()
                        .addInterceptor(httpLoggingInterceptor).build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static FirebaseMessagingService getService() {
        FirebaseMessagingService firebaseMessagingService = getClient().create(FirebaseMessagingService.class);
        return firebaseMessagingService;
    }
}
