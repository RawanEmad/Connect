package com.example.connect.users.api;

import com.example.connect.users.LoginRequest;
import com.example.connect.users.LoginResponse;
import com.example.connect.users.RegisterRequest;
import com.example.connect.users.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsersApiService {

    @POST("api/users/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest, @Header("api_key") String api_key);

    @POST("api/users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest, @Header("api_key") String api_key);

}
