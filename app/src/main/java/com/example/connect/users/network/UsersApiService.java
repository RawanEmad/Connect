package com.example.connect.users.network;

import com.example.connect.users.model.UserListResponse;
import com.example.connect.users.model.UserModel;
import com.example.connect.users.model.UserResponse;
import com.example.connect.users.request.LoginRequest;
import com.example.connect.users.request.RegisterRequest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UsersApiService {

    @POST("api/users/")
    Call<UserModel> registerUser(@Body RegisterRequest registerRequest, @Header("api_key") String api_key);

    @POST("api/users/login")
    Call<UserModel> loginUser(@Body LoginRequest loginRequest, @Header("api_key") String api_key);

    @GET("api/users")
    Call<UserListResponse> getAllUsers(@Header("api_key") String api_key);

    @GET("api/users/{mobile}")
    Call<UserResponse> getUser(@Path("mobile") String mobile, @Header("api_key") String api_key);

    @Multipart
    @Headers({
            "Content-Type: multipart/form-data",
            "Accept: application/json"
    })
    @PATCH("api/users/image/{mobile}")
    Call<UserModel> uploadUserImage(@Path("mobile") String mobile, @Part MultipartBody.Part image, @Header("api_key") String api_key);
}
