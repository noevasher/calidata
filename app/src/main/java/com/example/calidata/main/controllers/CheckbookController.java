package com.example.calidata.main.controllers;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.CheckbookArrayModel;
import com.example.calidata.models.CheckbookModel;
import com.example.calidata.models.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class CheckbookController extends ParentController {

    public CheckbookController(Context context) {
        super(context);
    }


    public Single<CheckbookArrayModel> getCheckbooks(String token, Integer userId) {
        return Single.create(emitter -> {
            HashMap<String, Object> map = new HashMap();
            map.put("usuarioId", userId);
            try {
                Call<CheckbookArrayModel> call = restClient.getCheckbookByUserId(token, map);
                //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

                call.enqueue(new Callback<CheckbookArrayModel>() {
                    @Override
                    public void onResponse(Call<CheckbookArrayModel> call, Response<CheckbookArrayModel> response) {
                        if (response.code() == 200) {
                            CheckbookArrayModel data = response.body();
                            emitter.onSuccess(data);
                        } else {
                            Throwable throwable = new Exception(response.message());
                            emitter.onError(throwable);
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckbookArrayModel> call, Throwable t) {
                        Log.e("error", t.toString());
                        emitter.onError(t);
                    }
                });
            }
            catch (Exception e){
                Log.e("error", e.getMessage());
                e.printStackTrace();

            }
        });
        //*/
    }

    public Single<CheckbookModel> addCheckbook(String token, String checkId) {
        return Single.create(emitter -> {
            try {
                HashMap<String, Object> body = new HashMap<>();
                body.put("checkId", checkId);
                try {
                    Call<CheckbookModel> call = restClient.addCheckBook(token, body);
                    call.enqueue(new Callback<CheckbookModel>() {
                        @Override
                        public void onResponse(Call<CheckbookModel> call, Response<CheckbookModel> response) {
                            if (response.code() == 200) {
                                CheckbookModel data = response.body();
                                emitter.onSuccess(data);
                            } else {
                                Throwable throwable = new Exception(response.message());
                                emitter.onError(throwable);
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckbookModel> call, Throwable t) {
                            Log.e("error", t.toString());
                            emitter.onError(t);
                        }
                    });
                }catch(Exception e){
                    Log.e("error", e.getMessage());
                    e.printStackTrace();
                }
            }
            catch(Exception e){
                e.getStackTrace();
                Log.e("ERROR", e.getMessage());
            }

        });
        //*/
    }

    public Single<User> getUserInformation(Double userId) {
        return Single.create(emitter -> {
            Call<User> call = restClient.getUserInformation(userId);
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
}
