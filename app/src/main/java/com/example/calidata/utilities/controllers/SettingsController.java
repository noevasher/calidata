package com.example.calidata.utilities.controllers;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsController extends ParentController {

    public SettingsController(Context context) {
        super(context);
    }

    public Single<String> getTermsConditions(String token, Integer bankId) {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("Config", generateMapData());

            Call<HashMap<String, Object>> call = restClient.getTermsConditions(token, bankId, body);
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<HashMap<String, Object>>() {
                @Override
                public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                    if (response.code() == 200) {
                        HashMap<String, Object> responseStructure = response.body();
                        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) responseStructure.get("data");
                        String responseTerms = (String) data.get("terms");
                        emitter.onSuccess(responseTerms);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }

    public Single<String> getPrivacyTerms(String token, Integer bankId) {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("Config", generateMapData());
            Call<HashMap<String, Object>> call = restClient.getPrivacyTerms(token, bankId, body);
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<HashMap<String, Object>>() {
                @Override
                public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                    if (response.code() == 200) {
                        HashMap<String, Object> responseStructure = response.body();
                        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) responseStructure.get("data");
                        String responseTerms = (String) data.get("privaci");
                        emitter.onSuccess(responseTerms);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }

    public Single<String> getContactBank(String token, Integer bankId) {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("Config", generateMapData());

            Call<HashMap<String, Object>> call = restClient.getContactBank(token, bankId, body);
            //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

            call.enqueue(new Callback<HashMap<String, Object>>() {
                @Override
                public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                    if (response.code() == 200) {
                        HashMap<String, Object> responseStructure = response.body();
                        LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) responseStructure.get("data");
                        String responseTerms = (String) data.get("contact");
                        emitter.onSuccess(responseTerms);
                    } else {
                        Throwable throwable = new Exception(response.message());
                        emitter.onError(throwable);
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                    Log.e("error", t.toString());
                    emitter.onError(t);
                }
            });
        });
        //*/
    }

}
