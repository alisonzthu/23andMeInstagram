package com.example.macstudio.instagramalison.api.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by macstudio on 1/4/18.
 */

public class SharedPrefManager {

    private static SharedPrefManager managerInstance;
    private static Context context;
    public static final String SHARED_PREFERENCE_NAME = "myPref1";
    // are KEY_USERNAME and KEY_USERID necessary?
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "userId";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (managerInstance == null) {
            managerInstance = new SharedPrefManager(context);
        }
        return managerInstance;
    }

    public boolean userLogin(String access_token) {
        // MODE_PRIVATE means only this app can access this SharedPrefernce
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, access_token);
        // apply() is asynchronou and commit() is synchronous.
        // One shouldn't call commit() from the UI thread
        editor.apply();

        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_ACCESS_TOKEN, null) != null) {
            return true;
        }
        return false;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.apply();
        Log.d("logging out", "successful");
        return true;
    }

}
