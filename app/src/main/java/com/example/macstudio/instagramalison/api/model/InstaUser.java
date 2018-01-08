package com.example.macstudio.instagramalison.api.model;

/**
 * Created by macstudio on 1/9/18.
 */

public class InstaUser {

    private String id;
    private String username;
    private String full_name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
