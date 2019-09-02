package com.example.calidata.main.controllers;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.CheckbookModel;
import com.example.calidata.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckbookController extends ParentController {

    public CheckbookController(Context context) {
        super(context);
    }


    public Single<List<CheckbookModel>> getCheckbooks(Double userId) {
        return Single.create(emitter -> {
            Call<List<CheckbookModel>> call = restClient.getCheckbookByUserId(userId);
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<List<CheckbookModel>>() {
                @Override
                public void onResponse(Call<List<CheckbookModel>> call, Response<List<CheckbookModel>> response) {
                    if (response.code() == 200) {
                        List<CheckbookModel> data = response.body();
                        emitter.onSuccess(data);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<List<CheckbookModel>> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }

    public Single<User> getUserInformation(Integer userId) {
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
