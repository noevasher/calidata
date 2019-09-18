package com.example.calidata.retrofit;

import com.example.calidata.models.BankModel;
import com.example.calidata.models.CheckArrayModel;
import com.example.calidata.models.CheckModel;
import com.example.calidata.models.CheckbookArrayModel;
import com.example.calidata.models.CheckbookModel;
import com.example.calidata.models.LoginResponse;
import com.example.calidata.models.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("banco/listadoBanco")
    Call<List<BankModel>> getBanks();

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("autenticacion")
    Call<LoginResponse> authentication(@Header("Usuario") String user, @Header("IdPass")String password, @Field("grant_type") String type);

    @Headers("Content-Type: application/json")
    @POST("cheque/AgregarChequera")
    Call<CheckbookModel> addCheckBook(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST("cheque/LiberaCheque")
    Call<CheckbookModel> emitCheck(@Header("Authorization") String token,
                                   @Body HashMap<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST("cheque/ObtenerCheques")
    Call<CheckArrayModel> getChecksByUserId(@Header("Authorization") String token,
                                            @Body HashMap<String, Object> body);

    @POST("account/register")
    @Headers("Content-Type: application/json")
    Call<User> registerUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("cheque/ObtenerChequera")
    Call<CheckbookArrayModel> getCheckbookByUserId(@Header("Authorization") String token,
                                                   @Body HashMap<String, Object> body);

    @GET("user/info")
    Call<User> getUserInformation(@Path("id") Double userId);

    @POST("cheque/ActivarCheckId")
    @Headers("Content-Type: application/json")
    Call<CheckbookModel> activeCheckId(@Body User user);

    @POST("cheque/CancelaCheckId")
    @Headers("Content-Type: application/json")
    Call<CheckModel> cancelCheckId(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    @POST("cheque/LiberaCheque")
    @Headers("Content-Type: application/json")
    Call<CheckbookModel> freeCheck(@Body User user);

    @POST("cheque/ConsultarEstatusCheckId")
    @Headers("Content-Type: application/json")
    Call<CheckbookModel> getStatusCheckId(@Body User user);



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