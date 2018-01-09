package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.model.SelfLikeMediaResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by macstudio on 1/8/18.
 */

public interface LikeService {
    @GET("v1/media/{media-id}/likes")
    Call<SelfLikeMediaResponse> getMediaLikes(@Path("media-id") String mediaId,
                                              @Query("access_token") String access_token);

    @POST("v1/media/{media-id}/likes")
    Call<SelfLikeMediaResponse> postLikeMedia();

    @DELETE("v1/media/{media-id}/likes")
    Call<SelfLikeMediaResponse> deleteLikeMedia();

    //should finish the post and delete methods, then complete the SelfLikeMediaResponse class
}
