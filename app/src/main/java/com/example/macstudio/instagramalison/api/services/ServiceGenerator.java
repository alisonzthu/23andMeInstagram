package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.ApplicationConsts;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macstudio on 12/18/17.
 */

public class ServiceGenerator {

    public static GetTokenService createTokenService() {
        return getRetrofit().create(GetTokenService.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ApplicationConsts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
