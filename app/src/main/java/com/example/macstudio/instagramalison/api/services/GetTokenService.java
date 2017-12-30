package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by macstudio on 12/30/17.
 */

public interface GetTokenService {
    // add tag_name later!
    @GET("v1/tags/{tag_name}/media/recent")
    Call<TokenResponse> getAccessToken(@Path("tag_name") String tag_name,
            @Query("access_token") String access_token);
}
