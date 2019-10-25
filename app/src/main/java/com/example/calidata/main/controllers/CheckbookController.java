package com.example.calidata.main.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.calidata.main.CheckbookActivity;
import com.example.calidata.main.ParentController;
import com.example.calidata.models.CheckArrayModel;
import com.example.calidata.models.CheckModel;
import com.example.calidata.models.CheckbookArrayModel;
import com.example.calidata.models.CheckbookModel;
import com.example.calidata.models.User;
import com.example.calidata.session.SessionManager;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckbookController extends ParentController {

    public CheckbookController(Context context) {
        super(context);
    }


    public Single<CheckbookArrayModel> getCheckbooks(String token, Integer userId) {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap();
            body.put("idUsuario", userId);
            body.put("Config", generateMapData());
            System.out.println("exito al generar mapa");
            try {
                Call<CheckbookArrayModel> call = restClient.getCheckbookByUserId(token, body);
                ((CheckbookActivity) mContext).progressBar.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<CheckbookArrayModel>() {
                    @Override
                    public void onResponse(Call<CheckbookArrayModel> call, Response<CheckbookArrayModel> response) {
                        if (response.code() == 200) {
                            CheckbookArrayModel data = response.body();
                            emitter.onSuccess(data);
                        } else {
                            String message = response.message();
                            /*
                            if(message.equals("Unauthorized")){

                            }
                            //*/
                            Throwable throwable = new Exception(message);
                            emitter.onError(throwable);
                            ((CheckbookActivity) mContext).progressBar.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onFailure(Call<CheckbookArrayModel> call, Throwable t) {
                        Log.e("error", t.toString());
                        emitter.onError(t);
                        ((CheckbookActivity) mContext).progressBar.setVisibility(View.GONE);

                    }
                });
            } catch (Exception e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
                ((CheckbookActivity) mContext).progressBar.setVisibility(View.GONE);

            }
        });
        //*/
    }

    public Single<CheckbookModel> addCheckbook(String token, String checkId, Integer userId) {
        return Single.create(emitter -> {
            try {
                HashMap<String, Object> body = new HashMap<>();
                //body.put("checkId", checkId);
                body.put("ID_CheckID", checkId);
                body.put("idUsuario", userId);
                body.put("Config", generateMapData());

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

    public Single<User> getUserInformation(Integer userId) {
        return Single.create(emitter -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("Config", generateMapData());
            Call<User> call = restClient.getUserInformation(userId, body);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User data = response.body();
                        data.setUserName((String) response.body().getData().get("usuario"));
                        String image = (String) response.body().getData().get("image64");
                        if (image != null && !image.isEmpty()) {
                            data.setImage64(image);
                        }
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

    public Single<User> saveProfile(HashMap<String, Object> body) {
        return Single.create(emitter -> {
            String token = SessionManager.getInstance(mContext).getToken();
            body.put("Config", generateMapData());
            Call<User> call = restClient.saveProfile(token, body);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User data = response.body();
                        data.setUserName((String) response.body().getData().get("usuario"));
                        String image = (String) response.body().getData().get("image64");
                        if (image != null && !image.isEmpty()) {
                            data.setImage64(image);
                        }
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

    public Single<CheckbookModel> emitCheckId(String token, HashMap<String, Object> body) {
        return Single.create(emitter -> {
            try {
                body.put("Config", generateMapData());
                Call<CheckbookModel> call = restClient.emitCheck(token, body);
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
            } catch (Exception e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }

        });
        //*/
    }

    public Single<CheckArrayModel> getChecksByUserId(String token, Integer userId) {
        return Single.create(emitter -> {
            try {
                HashMap<String, Object> body = new HashMap<>();
                body.put("idUsuario", userId);
                body.put("Config", generateMapData());


                try {
                    Call<CheckArrayModel> call = restClient.getChecksByUserId(token, body);
                    call.enqueue(new Callback<CheckArrayModel>() {
                        @Override
                        public void onResponse(Call<CheckArrayModel> call, Response<CheckArrayModel> response) {
                            if (response.code() == 200) {
                                CheckArrayModel data = response.body();
                                emitter.onSuccess(data);
                            } else {
                                Throwable throwable = new Exception(response.message());
                                emitter.onError(throwable);
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckArrayModel> call, Throwable t) {
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

    public Single<CheckArrayModel> getCheckById(String token, HashMap<String, Object> body) {
        return Single.create(emitter -> {
            try {
                body.put("Config", generateMapData());

                Call<CheckArrayModel> call = restClient.getCheckById(token, body);
                call.enqueue(new Callback<CheckArrayModel>() {
                    @Override
                    public void onResponse(Call<CheckArrayModel> call, Response<CheckArrayModel> response) {
                        if (response.code() == 200) {
                            CheckArrayModel data = response.body();
                            emitter.onSuccess(data);
                        } else {
                            Throwable throwable = new Exception(response.message());
                            emitter.onError(throwable);
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckArrayModel> call, Throwable t) {
                        Log.e("error", t.toString());
                        emitter.onError(t);
                    }
                });
            } catch (Exception e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public Single<CheckArrayModel> getChecksByUserIdAndCheckId(String token, String checkId, Integer userId) {
        return Single.create(emitter -> {
            try {
                HashMap<String, Object> body = new HashMap<>();
                body.put("idUsuario", userId);
                body.put("ID_CheckID", checkId);
                body.put("Config", generateMapData());

                try {
                    Call<CheckArrayModel> call = restClient.getChecksByUserIdAndCheckId(token, body);
                    call.enqueue(new Callback<CheckArrayModel>() {
                        @Override
                        public void onResponse(Call<CheckArrayModel> call, Response<CheckArrayModel> response) {
                            if (response.code() == 200) {
                                CheckArrayModel data = response.body();
                                emitter.onSuccess(data);
                            } else {
                                Throwable throwable = new Exception(response.message());
                                emitter.onError(throwable);
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckArrayModel> call, Throwable t) {
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

    public Single<CheckArrayModel> getChecksByFilters(String token, HashMap<String, Object> body) {
        return Single.create(emitter -> {
            try {
                body.put("Config", generateMapData());

                Call<CheckArrayModel> call = restClient.getChecksByUserIdAndCheckId(token, body);
                call.enqueue(new Callback<CheckArrayModel>() {
                    @Override
                    public void onResponse(Call<CheckArrayModel> call, Response<CheckArrayModel> response) {
                        if (response.code() == 200) {
                            CheckArrayModel data = response.body();
                            emitter.onSuccess(data);
                        } else {
                            Throwable throwable = new Exception(response.message());
                            emitter.onError(throwable);
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckArrayModel> call, Throwable t) {
                        Log.e("error", t.toString());
                        emitter.onError(t);
                    }
                });
            } catch (Exception e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }
        });
        //*/
    }

    public Single<CheckModel> cancelCheckbook(String token, HashMap<String, Object> body) {
        return Single.create(emitter -> {
            try {
                body.put("Config", generateMapData());
                Call<CheckModel> call = restClient.cancelCheckbook(token, body);
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

        });
        //*/
    }

}
