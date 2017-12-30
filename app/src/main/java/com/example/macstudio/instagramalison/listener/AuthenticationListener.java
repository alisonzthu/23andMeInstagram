package com.example.macstudio.instagramalison.listener;

/**
 * Created by macstudio on 12/18/17.
 */

public interface AuthenticationListener {
    public abstract void onCodeReceived(String code);
    public abstract void onError(String error);
}
