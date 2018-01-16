package com.example.macstudio.instagramalison.database;

/**
 * Created by azhang on 1/12/18.
 */

public class FeedModel {
    public int id;
    public String username;
    public String avatar;
    public String image;
    // whether the user herself likes the current media
    public Integer likeStatus;
    public Integer likeCount;
    public String mediaId;
}
