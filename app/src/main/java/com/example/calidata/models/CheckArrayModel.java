package com.example.calidata.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class CheckArrayModel {
    public List<HashMap<String, Object>> data;

    @SerializedName("exito")
    private Boolean success;

    @SerializedName("mensaje")
    private String message;

    public CheckArrayModel() {

    }

    public List<HashMap<String, Object>> getData() {
        return data;
    }

    public void setData(List<HashMap<String, Object>> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
