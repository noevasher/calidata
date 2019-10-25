package com.example.calidata.utilities.controllers;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.User;
import com.example.calidata.session.SessionManager;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController extends ParentController {

    public UserController(Context context) {
        super(context);
    }

    public Single<User> saveProfile(HashMap<String, Object> body) {
        return Single.create(emitter -> {
            String token = SessionManager.getInstance(mContext).getToken();
            body.put("Config", generateMapData());
            Call<User> call = restClient.saveProfile(token, body);
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User data = response.body();
                        emitter.onSuccess(data);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }

    public Single<User> changePassword(String token, HashMap<String, Object> body) {
        return Single.create(emitter -> {
            Call<User> call = restClient.changePassword(token, body);
            body.put("Config", generateMapData());

            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User data = response.body();
                        emitter.onSuccess(data);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }

    public Single<User> getUserInformation(Integer userId) {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("Config", generateMapData());
            Call<User> call = restClient.getUserInformation(userId, body);
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User data = response.body();
                        data.setUserName((String) response.body().getData().get("usuario"));
                        String image = (String) response.body().getData().get("image64");
                        String phone = (String) response.body().getData().get("celular");
                        if (image != null && !image.isEmpty()) {
                            data.setImage64(image);
                        }
                        data.setPhone(phone.trim());
                        emitter.onSuccess(data);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }


    public Single<String> forgotPassword(HashMap<String, Object> body) {
        return Single.create(emitter -> {
            Call<User> call = restClient.forgotPassword(body);
            body.put("Config", generateMapData());

            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        String data = response.body().getMessage();
                        emitter.onSuccess(data);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }
}
