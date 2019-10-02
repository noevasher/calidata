package com.example.calidata.activities;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.CheckModel;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckController extends ParentController {

    public CheckController(Context context) {
        super(context);
    }

    public Single<CheckModel> cancelCheckId(String token, HashMap<String, Object> body) {
        return Single.create(emitter -> {
            try {

                try {
                    Call<CheckModel> call = restClient.cancelCheckId(token, body);
                    call.enqueue(new Callback<CheckModel>() {
                        @Override
                        public void onResponse(Call<CheckModel> call, Response<CheckModel> response) {
                            if (response.code() == 200) {
                                CheckModel data = response.body();
                                emitter.onSuccess(data);
                            } else {
                                Throwable throwable = new Exception(response.message());
                                emitter.onError(throwable);
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckModel> call, Throwable t) {
                            Log.e("error", t.toString());
                            emitter.onError(t);
                        }
                    });
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.getStackTrace();
                Log.e("ERROR", e.getMessage());
            }

        });
        //*/
    }
}
