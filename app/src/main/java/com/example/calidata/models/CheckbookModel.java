package com.example.calidata.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class CheckbookModel {
    public HashMap<String, Object> data;
    private String checkbookId;
    private String checkId;
    private String typeDoc;

    @SerializedName("mensaje")
    private String message;

    @SerializedName("exito")
    private Boolean success;

    public CheckbookModel() {

    }

    public String getCheckbookId() {
        return checkbookId;
    }

    public void setCheckbookId(String checkbookId) {
        this.checkbookId = checkbookId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
