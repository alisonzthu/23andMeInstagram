package com.example.macstudio.instagramalison.api.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;


/**
 * Created by macstudio on 1/4/18.
 */

public class SharedPrefManager {

    private static SharedPrefManager managerInstance;
    private static final String TAG = SharedPrefManager.class.getSimpleName();
    private static Context context;
    public static final String SHARED_PREFERENCE_NAME = "myPref1";
    public static final String KEY_ACCESS_TOKEN = "access_token";

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
        // apply() is asynchronous and commit() is synchronous.
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        }
        Log.i(TAG,"User successfully logged out");
        return true;
    }

}
