package com.example.calidata.main;

import android.content.Context;
import android.util.Log;

import com.example.calidata.models.BankModel;
import com.example.calidata.retrofit.JsonPlaceHolderApi;
import com.example.calidata.retrofit.RetrofitManager;
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

    public Single<List<BankModel>> getBanks() {


        return Single.create(emitter -> {
            Call<List<BankModel>> call = restClient.getBanks();
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<List<BankModel>>() {
                @Override
                public void onResponse(Call<List<BankModel>> call, Response<List<BankModel>> response) {
                    if (response.code() == 200) {
                        List<BankModel> data = response.body();
                        String listResponse = new Gson().toJson(response.body());

                        emitter.onSuccess(data);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<List<BankModel>> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
    }

}
