package com.example.connect.users.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private UserModel userData;

    public UserResponse(String status, UserModel userData) {
        this.status = status;
        this.userData = userData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserModel getUserData() {
        return userData;
    }

    public void setUserData(UserModel userData) {
        this.userData = userData;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "status='" + status + '\'' +
                ", userData=" + userData +
                '}';
    }
}
