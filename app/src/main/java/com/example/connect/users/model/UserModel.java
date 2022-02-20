package com.example.connect.users.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserModel {

    @SerializedName("_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String fullName;
    @SerializedName("mobile")
    @Expose
    private String phoneNo;
    @SerializedName("gender")
    @Expose
    private String gender;

    public UserModel(String userId, String fullName, String phoneNo, String gender) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.gender = gender;
    }

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

    @Override
    public String toString() {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
