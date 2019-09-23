package com.example.calidata.models;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class User {
    private static User instance = null;
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
    public String accessToken;

    public String tokenType;


    public static User getInstance() {
        if (instance == null) {
            return new User();
        }
        return instance;
    }

    public static User getInstance(String userName, String email, String password, Integer bankId) {
        if (instance == null) {
            return new User(userName, email, password, bankId);
        }
        return instance;
    }

    public static User getInstance(Double userId, String email, Integer bankId) {
        if (instance == null) {
            return new User(userId, email, bankId);
        }
        return instance;
    }

    private User() {

    }

    private User(String userName, String email, String password, Integer bankId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.bankId = bankId;
    }

    private User(Double userId, String email, Integer bankId) {
        this.userId = userId;
        this.email = email;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
