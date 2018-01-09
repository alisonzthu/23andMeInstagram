package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.AppConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macstudio on 12/18/17.
 */

public class ServiceGenerator {

    public static GetUserDataService createUserDataService() {
        return getRetrofit().create(GetUserDataService.class);
    }

    public static GetFeedService createGetFeedService() {
        return getRetrofit().create(GetFeedService.class);
    }

    public static LikeService createLikeService() {
        return getRetrofit().create(LikeService.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
