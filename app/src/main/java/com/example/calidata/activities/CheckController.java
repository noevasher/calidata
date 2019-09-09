package com.example.calidata.activities;

import android.content.Context;
import android.util.Log;

import com.example.calidata.main.ParentController;
import com.example.calidata.models.CheckModel;
import com.example.calidata.models.CheckbookModel;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckController extends ParentController {

    public CheckController(Context context) {
        super(context);
    }

    public Single<CheckModel> cancelCheckId(String token, String checkId) {
        return Single.create(emitter -> {
            HashMap<String, Object> map = new HashMap();
            map.put("checkId", checkId);
            try {
                Call<CheckModel> call = restClient.cancelCheckId(token, map);
                //Call<LoginResponse> call = restClient.authentication(user,password, GRANT_TYPE);

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
            }
            catch (Exception e){
                Log.e("error", e.getMessage());
                e.printStackTrace();

            }
        });
        //*/
    }
}
