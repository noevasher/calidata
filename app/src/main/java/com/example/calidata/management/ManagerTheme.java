package com.example.calidata.management;

import com.example.calidata.session.SessionManager;

public class ManagerTheme {
    private static ManagerTheme instance = null;
    private int themeId;
    private String bankName;
    private int firstTheme;

    private ManagerTheme() {

    }

    public static ManagerTheme getInstance() {
        if (instance == null)
            instance = new ManagerTheme();

        return instance;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public void saveFirstTheme(int themeResId) {
        this.firstTheme = themeResId;
    }

    public int getFirstTheme() {
        return this.firstTheme;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
