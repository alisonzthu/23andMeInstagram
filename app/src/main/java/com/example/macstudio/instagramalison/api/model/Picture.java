package com.example.macstudio.instagramalison.api.model;

import java.io.Serializable;

/**
 * Created by macstudio on 12/30/17.
 */

public class Picture implements Serializable {
    private String url;

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }
}
