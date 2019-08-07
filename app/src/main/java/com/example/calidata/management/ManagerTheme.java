package com.example.calidata.management;

public class ManagerTheme {
    private static ManagerTheme instance = null;
    private int themeId;

    public ManagerTheme(){

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
}
