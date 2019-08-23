package com.example.calidata.management;

public class ManagerTheme {
    private static ManagerTheme instance = null;
    private int themeId;
    private int firstTheme;

    public ManagerTheme() {

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
}
