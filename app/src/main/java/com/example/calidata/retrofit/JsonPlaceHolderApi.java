package com.example.calidata.retrofit;

import com.example.calidata.models.LoginResponse;
import com.example.calidata.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("/posts/1")
    Call<LoginResponse> authentication();

    //@Headers("userID : 17E9817988718E7187E710E")
    @Headers("Content-type: application/json; charset=UTF-8")
    @POST("/posts")
    Call<User> registerUser(@Body User data);



}