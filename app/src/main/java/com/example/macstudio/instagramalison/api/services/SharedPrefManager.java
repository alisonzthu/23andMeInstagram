package com.example.macstudio.instagramalison.api.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;

import com.example.macstudio.instagramalison.database.FeedContract;


/**
 * Created by macstudio on 1/4/18.
 */

public class SharedPrefManager {

    private static SharedPrefManager managerInstance;
    private static final String TAG = SharedPrefManager.class.getSimpleName();
    private Context context;
    public static final String SHARED_PREFERENCE_NAME = "myPref1";
    public static final String KEY_ACCESS_TOKEN = "access_token";

    private SharedPrefManager(Context context) {
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
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null) != null;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.apply();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        }
        // drop db
        context.deleteDatabase(FeedContract.DB_NAME);

        Log.i(TAG,"User successfully logged out");
        return true;
    }

}
