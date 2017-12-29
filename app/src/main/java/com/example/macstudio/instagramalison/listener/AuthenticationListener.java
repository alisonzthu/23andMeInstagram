package com.example.macstudio.instagramalison.listener;

/**
 * Created by macstudio on 12/18/17.
 */

public interface AuthenticationListener {
    public abstract void onComplete(String accessToken);
    public abstract void onError(String error);
}
