package com.example.calidata.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("title")
    public String title;
    @SerializedName("body")
    public String body;
    @SerializedName("userId")
    public Integer userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
