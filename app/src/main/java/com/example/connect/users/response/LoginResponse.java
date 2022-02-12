package com.example.connect.users.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("_id")
    private String userId;
    @SerializedName("name")
    private String fullName;
    @SerializedName("mobile")
    private String phoneNo;
    @SerializedName("gender")
    private String gender;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
