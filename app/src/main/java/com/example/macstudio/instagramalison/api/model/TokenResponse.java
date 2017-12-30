package com.example.macstudio.instagramalison.api.model;

/**
 * Created by macstudio on 12/30/17.
 */

public class TokenResponse {
    //    private User user;
    private String access_token;
    private String code;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

//    public User getUser() {
//        return user;
//    }
}
