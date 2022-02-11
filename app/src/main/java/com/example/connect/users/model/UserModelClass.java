package com.example.connect.users.model;

import com.google.gson.annotations.SerializedName;

public class UserModelClass {

    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String fullName;
    @SerializedName("mobile")
    private String phoneNo;
    @SerializedName("password")
    private String password;
    @SerializedName("gender")
    private String gender;

    public UserModelClass() {
    }

    public UserModelClass(String fullName, String phoneNo, String password, String gender) {
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.password = password;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
