package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.model.InstagramResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by macstudio on 1/3/18.
 */

public interface GetFeedService {
    @GET("v1/users/self/media/recent")
    Call<InstagramResponse> getOwnPhotos(@Query("access_token") String access_token);
}
