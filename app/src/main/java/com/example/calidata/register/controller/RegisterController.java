package com.example.calidata.register.controller;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.Bank;
import com.example.calidata.models.LoginResponse;
import com.example.calidata.models.User;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterController extends ParentController {

    private User user;

    public RegisterController(User user, Context context) {
        super(context);
        this.user = user;
    }

    public RegisterController(Context context) {
        super(context);
    }



    public Single<User> registerUserByBody(User user) {
        return Single.create(emitter -> {
           Call<User> call = restClient.registerUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User data = response.body();
                        if(response.body().isSuccess){
                            emitter.onSuccess(data);
                        }
                        else{
                            String message = response.body().getMessage();
                            emitter.onError(new Exception(message));
                        }

                    } else {
                        emitter.onError(new Exception(response.message()));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("error", t.toString());
                }
            });
        });

    }




}
