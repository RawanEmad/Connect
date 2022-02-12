package com.example.connect.users.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("mobile")
    private String phoneNo;
    @SerializedName("password")
    private String password;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
