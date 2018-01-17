package com.example.macstudio.instagramalison.listener;

/**
 * Created by macstudio on 12/18/17.
 */

public interface AuthenticationListener {
    void onTokenReceived(String access_token);
    void onError(String error);
    void onNoInternet();
}
