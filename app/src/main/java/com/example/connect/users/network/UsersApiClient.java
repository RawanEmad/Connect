package com.example.connect.users.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersApiClient {

    public static final String BASE_URL = "https://Api.connect-asl.site/";
    //API KEY
    public final static String API_KEY = "64a2fb17debfd9dd178b80e5d31d4efe84b6dd14971ea836";
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

    public static UsersApiService getService() {
        UsersApiService usersApiService = getClient().create(UsersApiService.class);
        return usersApiService;
    }

}
