package com.example.calidata.retrofit;

import android.content.Intent;

import com.example.calidata.models.Bank;
import com.example.calidata.models.LoginRequest;
import com.example.calidata.models.LoginResponse;
import com.example.calidata.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("banco/listadoBanco")
    Call<List<Bank>> getBanks();

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("autenticacion")
    Call<LoginResponse> authentication(@Header("Usuario") String user, @Header("IdPass")String password, @Field("grant_type") String type);


    @POST("account/register")
    @Headers("Content-Type: application/json")
    Call<User> registerUser(@Body User user);


    /*
    @Headers("Static-Al:123 ")
    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@Field("userId") int userId, @Field("title") String title);

    @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") int id, @Body Post post);

    @PATCH("posts/{id}")
    Call<Post> patchPost(@Path("id") int id, @Body Post post);
//*/



}