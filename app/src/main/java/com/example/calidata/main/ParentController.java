package com.example.calidata.main;

import com.example.calidata.retrofit.JsonPlaceHolderApi;
import com.example.calidata.retrofit.RetrofitManager;

public class ParentController {

    protected RetrofitManager retrofitManager;
    protected JsonPlaceHolderApi restClient;

    public ParentController() {
        retrofitManager = RetrofitManager.getInstance();
        restClient = retrofitManager.getRetrofit().create(JsonPlaceHolderApi.class);

    }


}
