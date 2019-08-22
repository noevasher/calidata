package com.example.calidata.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    public String grant_type;

    public LoginRequest(String password){
        this.grant_type = password;
    }

}
