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

    @GET("banco/listadoBanco")
    Call<List<BankModel>> getBanks();

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

    @Headers("Content-Type: application/json")
    @POST("cheque/ObtenerCheques")
    Call<CheckArrayModel> getChecksByUserIdAndCheckId(@Header("Authorization") String token,
                                                      @Body HashMap<String, Object> body);

    @Headers("Content-Type: application/json")
    @POST("cheque/ObtenerChequera")
    Call<CheckbookArrayModel> getCheckbookByUserId(@Header("Authorization") String token,
                                                   @Body HashMap<String, Object> body);

    @POST("cheque/CancelaCheckId")
    @Headers("Content-Type: application/json")
    Call<CheckModel> cancelCheckId(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    //-----------SERVICIOS DE USUARIO----------------//
    @GET("login/getProfileInfo")
    Call<User> getUserInformation(@Header("idUsuario") Integer userId);

    @POST("login/saveProfileInfo")
    Call<User> saveProfile(@Body HashMap<String, Object> body);

    @POST("login/changePassword")
    @Headers("Content-Type: application/json")
    Call<User> changePassword(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    @POST("login/forgotPassword")
    @Headers("Content-Type: application/json")
    Call<User> forgotPassword(@Body HashMap<String, Object> body);

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("autenticacion")
    Call<LoginResponse> authentication(@Header("Usuario") String user, @Header("IdPass") String password, @Field("grant_type") String type);

    @POST("account/register")
    @Headers("Content-Type: application/json")
    Call<User> registerUser(@Body User user);



}