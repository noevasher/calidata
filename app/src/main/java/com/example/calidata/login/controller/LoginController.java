package com.example.calidata.login.controller;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.LoginRequest;
import com.example.calidata.models.LoginResponse;
import com.example.calidata.retrofit.Post;
import com.example.calidata.session.SessionManager;

import org.reactivestreams.Subscriber;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController extends ParentController {

    private static final String GRANT_TYPE = "password";
    public LoginController(Context context) {
        super(context);

    }

    //public Single<LoginResponse> loadJson(String user, String password) {
    public Single<LoginResponse> loadJson(String user, String password) {


        return Single.create(emitter -> {
            Call<LoginResponse> call = restClient.authentication(user,password, "password");
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.code() == 200){
                        LoginResponse data = response.body();
                        emitter.onSuccess(data);
                    }else{
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });

        //*/
    }


}
