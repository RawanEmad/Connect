package com.example.connect.users.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserModel {

    @SerializedName("status")
    private String status;
    @SerializedName("length")
    private int length;
    @SerializedName("users")
    private List<UserModel> usersList;

    @SerializedName("_id")
    private String userId;
    @SerializedName("name")
    private String fullName;
    @SerializedName("mobile")
    private String phoneNo;
    @SerializedName("gender")
    private String gender;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<UserModel> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UserModel> usersList) {
        this.usersList = usersList;
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
                ", fullName='" + fullName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
