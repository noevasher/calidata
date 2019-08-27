package com.example.calidata.register.controller;

import android.content.Context;
import android.util.Log;

import com.example.calidata.models.LoginResponse;
import com.example.calidata.main.ParentController;
import com.example.calidata.models.User;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterController extends ParentController {

    private User user;

    public RegisterController(User user, Context context){
        super(context);
        this.user = user;
    }

    public Single<User> loadJson(){
        Call<User> call = restClient.registerUser(this.user);

        return Single.create(emitter -> {
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 201) {
                        User data = response.body();
                        Log.i("DATA", "Usuario Creado: " + data.userId);
                        Log.i("DATA", "Data userId: " + data.body);
                        emitter.onSuccess(data);
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
