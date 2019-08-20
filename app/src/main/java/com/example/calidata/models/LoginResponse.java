package com.example.calidata.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("userId")
    public Integer userId;
    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;

    @SerializedName("body")
    public String body;

    /*
    public List<Body> data = null;

    public class Body {

        @SerializedName("id")
        public Integer id;
        @SerializedName("name")
        public String name;
        @SerializedName("year")
        public Integer year;
        @SerializedName("pantone_value")
        public String pantoneValue;

    }
    //*/
}
