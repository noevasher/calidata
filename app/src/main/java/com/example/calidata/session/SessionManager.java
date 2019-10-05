package com.example.calidata.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.calidata.login.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    private static SessionManager instance;

    public static SessionManager getInstance(Context context) {
        if (instance == null)
            instance = new SessionManager(context);
        return instance;
    }

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Pref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    private static final String KEY_NAME = "name";

    private static final String KEY_USER_ID = "userId";

    // Email address (make variable public to access from outside)
    private static final String KEY_EMAIL = "email";
    private static final String THEME_ID = "themeId";
    private static final String KEY_BANK_NAME = "bankName";
    private static final String KEY_BANK_ID = "bankId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IMAGE64 = "imageUser";
    private static final String KEY_TOKEN = "token";

    public static final String FIRST_THEME_ID = "themeId";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, int themeId) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        //editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.putInt(THEME_ID, themeId);

        // commit changes
        editor.commit();
    }

    public void createLoginSessionBank(String email, Integer bankId, String bankName, Integer userId, String username) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_BANK_NAME, bankName);
        editor.putInt(KEY_BANK_ID, bankId);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);


        // commit changes
        editor.commit();
    }

    public void createLoginSessionBank(String email, Integer bankId, String bankName, Integer userId) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_BANK_NAME, bankName);
        editor.putInt(KEY_BANK_ID, bankId);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putInt(KEY_USER_ID, userId);


        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public int getTheme() {
        //2 is default value
        return pref.getInt(THEME_ID, 2);

    }

    public String getBankName() {
        //2 is default value
        return pref.getString(KEY_BANK_NAME, null);

    }

    public String getKeyUsername() {
        //2 is default value
        return pref.getString(KEY_USERNAME, null);

    }

    public Integer getUserId() {
        try {
            Integer id = pref.getInt(KEY_USER_ID, 0);
            return id;
        }catch (Exception e){
            logoutUser();
            return 0;
        }

    }

    public void saveProfileImage(String image64) {
        editor.putString(KEY_IMAGE64, image64);
        // commit changes
        editor.commit();
    }

    public String getKeyImage64() {
        return pref.getString(KEY_IMAGE64, null);
    }


    public void saveProfileName(String username) {
        editor.putString(KEY_USERNAME, username);
        // commit changes
        editor.commit();
    }

    public void setAccessToken(String accessToken) {
        editor.putString(KEY_TOKEN, accessToken);
        // commit changes
        editor.commit();
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);

    }

    public Integer getBankId() {
        return pref.getInt(KEY_BANK_ID, 0);

    }
}