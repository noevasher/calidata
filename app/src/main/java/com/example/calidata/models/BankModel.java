package com.example.calidata.models;

import com.google.gson.annotations.SerializedName;

public class BankModel {
    @SerializedName("iD_BANCO")
    private int idBank;

    @SerializedName("deS_BANCO")
    private String nameBank;

    public BankModel(int idBank, String nameBank) {
        this.idBank = idBank;
        this.nameBank = nameBank;
    }

    public int getIdBank() {
        return idBank;
    }

    public String getNameBank() {
        return nameBank;
    }

}
