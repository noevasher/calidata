package com.example.calidata.models;


import com.google.gson.annotations.SerializedName;

public class User {
    private String userId;

    public String userName;

    public String email;

    public Integer bankId;

    public String password;

    @SerializedName("exito")
    public boolean isSuccess;

    @SerializedName("mensaje")
    public String message;

    public User(String userName, String email, String password, Integer bankId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.bankId = bankId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
}
