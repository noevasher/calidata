package com.example.calidata.main;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.calidata.models.BankModel;
import com.example.calidata.retrofit.JsonPlaceHolderApi;
import com.example.calidata.retrofit.RetrofitManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentController {

    private static final int LOCATION_CODE = 1000;
    protected Context mContext;
    private RetrofitManager retrofitManager;
    protected JsonPlaceHolderApi restClient;
    protected FusedLocationProviderClient mFusedLocationClient;

    public ParentController(Context context) {
        retrofitManager = RetrofitManager.getInstance();
        restClient = retrofitManager.getRetrofit().create(JsonPlaceHolderApi.class);
        mContext = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

    }

    public Single<List<BankModel>> getBanks() {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("Config", generateMapData());

            Call<List<BankModel>> call = restClient.getBanks(body);
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

    protected HashMap<String, Object> generateMapData() {
        String model = Build.MANUFACTURER + " " + Build.MODEL;
        String version = Build.VERSION.RELEASE;
        String SO = "Android " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        HashMap<String, Object> dataSystem = new HashMap<>();
        dataSystem.put("IP", getIP());
        dataSystem.put("SO", SO);
        dataSystem.put("Version", version);
        dataSystem.put("Modelo", model);
        dataSystem.put("geodatos", ParentActivity.getWayLatitude() + ", " + ParentActivity.getWayLongitude());

        return dataSystem;
    }

    private String getIP() {
        String publicIP = null;
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org")
                .openStream(), "UTF-8").useDelimiter("\\A")) {
            publicIP = s.next();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return publicIP;


    }


}
