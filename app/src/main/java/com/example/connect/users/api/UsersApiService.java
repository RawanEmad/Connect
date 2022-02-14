package com.example.connect.users.api;

import com.example.connect.users.model.UserResponse;
import com.example.connect.users.model.UserModel;
import com.example.connect.users.request.LoginRequest;
import com.example.connect.users.request.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UsersApiService {

    @POST("api/users/")
    Call<UserModel> registerUser(@Body RegisterRequest registerRequest, @Header("api_key") String api_key);

    @POST("api/users/login")
    Call<UserModel> loginUser(@Body LoginRequest loginRequest, @Header("api_key") String api_key);

    @GET("api/users")
    Call<UserResponse> getAllUsers(@Header("api_key") String api_key);

}
