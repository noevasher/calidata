package com.example.calidata.login.controller;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.LoginResponse;

import java.io.IOException;
import java.util.HashMap;

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
    public Single<LoginResponse> authentication(String email, String password) {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("Config", generateMapData());
            System.out.println("mapa auth: " + body);
            Call<LoginResponse> call = restClient.authentication(email, password, GRANT_TYPE);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.code() == 200) {
                        LoginResponse data = response.body();
                        emitter.onSuccess(data);


                    } else if (response.code() == 400) {
                        //Throwable throwable = new Exception(mContext.getString(R.string.error_invalid_user));
                        Throwable throwable = new Exception(response.message());
                        String error;
                        if (response.errorBody() != null) {
                            try {
                                error = response.errorBody().string();
                                error = error.replace("\"error\"", "")
                                        .replace(":", "")
                                        .replace("{", "")
                                        .replace("}", "");
                                throwable = new Exception(error);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        emitter.onError(throwable);
                    } else {
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
