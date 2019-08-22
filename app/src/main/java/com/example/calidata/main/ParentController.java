package com.example.calidata.main;

import android.content.Context;

import com.example.calidata.retrofit.JsonPlaceHolderApi;
import com.example.calidata.retrofit.RetrofitManager;
import com.example.calidata.session.SessionManager;

public class ParentController {


    protected Context mContext;
    protected RetrofitManager retrofitManager;
    protected JsonPlaceHolderApi restClient;

    public ParentController(Context context) {
        retrofitManager = RetrofitManager.getInstance();
        restClient = retrofitManager.getRetrofit().create(JsonPlaceHolderApi.class);
        mContext = context;
    }


}
