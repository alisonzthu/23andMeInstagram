package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.model.InstaUserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by macstudio on 1/9/18.
 */

public interface GetUserDataService {
    @GET("v1/users/self/")
    Call<InstaUserResponse> getUserProfile(@Query("access_token") String access_token);
}
