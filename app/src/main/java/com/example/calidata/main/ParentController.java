package com.example.calidata.main;

import android.content.Context;
import android.util.Log;

import com.example.calidata.models.Bank;
import com.example.calidata.retrofit.JsonPlaceHolderApi;
import com.example.calidata.retrofit.RetrofitManager;
import com.example.calidata.session.SessionManager;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentController {


    protected Context mContext;
    protected RetrofitManager retrofitManager;
    protected JsonPlaceHolderApi restClient;

    public ParentController(Context context) {
        retrofitManager = RetrofitManager.getInstance();
        restClient = retrofitManager.getRetrofit().create(JsonPlaceHolderApi.class);
        mContext = context;
    }

    public Single<List<Bank>> getBanks() {


        return Single.create(emitter -> {
            Call<List<Bank>> call = restClient.getBanks();
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<List<Bank>>() {
                @Override
                public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                    if (response.code() == 200) {
                        List<Bank> data = response.body();
                        String listResponse = new Gson().toJson(response.body());

                        emitter.onSuccess(data);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<List<Bank>> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
    }

}
