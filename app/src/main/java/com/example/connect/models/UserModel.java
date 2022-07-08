package com.example.connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("image")
    @Expose
    private String profileImage;
    @Expose
    @SerializedName("fcm")
    private String fcmToken;

    public UserModel(String userId, String fullName, String phoneNo, String gender, String profileImage, String fcmToken) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.profileImage = profileImage;
        this.fcmToken = fcmToken;
    }

    public UserModel() {}

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
