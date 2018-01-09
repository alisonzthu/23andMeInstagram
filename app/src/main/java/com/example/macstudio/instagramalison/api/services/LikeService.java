package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.model.SelfLikeMediaResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by macstudio on 1/8/18.
 */

public interface LikeService {

    @FormUrlEncoded
    @POST("v1/media/{media-id}/likes")
    Call<SelfLikeMediaResponse> postLikeMedia(@Path("media-id") String media_id,
                                              @Field("access_token") String access_token);

    @DELETE("v1/media/{media-id}/likes")
    Call<SelfLikeMediaResponse> deleteLikeMedia(@Path("media-id") String media_id,
                                                @Query("access_token") String access_token);

}
