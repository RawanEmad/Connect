package com.example.connect.users.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserListResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("length")
    @Expose
    private String length;

    @SerializedName("users")
    @Expose
    private List<UserModel> usersList;

    public UserListResponse(String status, String length, List<UserModel> usersList) {
        this.status = status;
        this.length = length;
        this.usersList = usersList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public List<UserModel> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UserModel> usersList) {
        this.usersList = usersList;
    }

    @Override
    public String toString() {
        return "UserListResponse{" +
                "status='" + status + '\'' +
                ", length=" + length +
                ", usersList=" + usersList +
                '}';
    }
}
