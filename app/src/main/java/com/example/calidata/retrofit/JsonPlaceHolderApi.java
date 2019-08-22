package com.example.calidata.retrofit;

import com.example.calidata.models.LoginRequest;
import com.example.calidata.models.LoginResponse;
import com.example.calidata.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*
    @GET("/posts/1")
    Call<LoginResponse> authentication();
    //*/

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("autenticacion")
    Call<LoginResponse> authentication(@Header("Usuario") String user, @Header("IdPass")String password, @Field("grant_type") String type);

    //@Headers("userID : 17E9817988718E7187E710E")
    @Headers("Content-type: application/json; charset=UTF-8")
    @POST("/posts")
    Call<User> registerUser(@Body User data);

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




}