package com.example.calidata.login.controller;

import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController extends ParentController {


    public LoginController() {
        super();
    }

    public void loadJSON() {

        Call<LoginResponse> call = restClient.authentication();

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                switch (response.code()) {
                    case 200:
                        LoginResponse data = response.body();
                        Log.i("DATA", "Data userId: " + data.userId);
                        Log.i("DATA", "Data userId: " + data.body);

                        break;
                    case 401:

                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }


}
