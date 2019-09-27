package com.example.calidata.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckModel {

    private String checkModelId;

    @SerializedName("iD_CheckID")
    private String checkId;

    @SerializedName("estatus")
    private String status;

    @SerializedName("monto")
    private Double quantity;

    @SerializedName("fecha")
    private String date;

    @SerializedName("descripcion")
    private String description;

    @SerializedName("exito")
    private Boolean success;

    @SerializedName("mensaje")
    public String message;

    public List data;

    public CheckModel() {

    }


    public String getCheckModelId() {
        return checkModelId;
    }

    public void setCheckModelId(String checkModelId) {
        this.checkModelId = checkModelId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

}
