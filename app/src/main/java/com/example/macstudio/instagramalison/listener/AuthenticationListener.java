package com.example.macstudio.instagramalison.listener;

/**
 * Created by macstudio on 12/18/17.
 */

public interface AuthenticationListener {
    public abstract void onTokenReceived(String access_token);
    public abstract void onError(String error);
}
