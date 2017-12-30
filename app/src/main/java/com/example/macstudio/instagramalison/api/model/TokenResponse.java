package com.example.macstudio.instagramalison.api.model;

/**
 * Created by macstudio on 12/30/17.
 */

public class TokenResponse {
    private Data.User user;
    private String access_token;

    public Data.User getUser() {
        return user;
    }

    public void setUser(Data.User user) {
        this.user = user;
    }

    public String getAccess_token() {
        return access_token;
    }
}
