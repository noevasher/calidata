package com.example.calidata.models;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class User {
    private Double userId;

    public String userName;

    public String email;

    public Integer bankId;

    public String password;

    @SerializedName("exito")
    public boolean isSuccess;

    @SerializedName("mensaje")
    public String message;

    @SerializedName("data")
    public HashMap<String, Object> data;

    public String image64;

    public User(String userName, String email, String password, Integer bankId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.bankId = bankId;
    }

    public Double getUserId() {
        return userId;
    }

    public void setUserId(Double userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
}
