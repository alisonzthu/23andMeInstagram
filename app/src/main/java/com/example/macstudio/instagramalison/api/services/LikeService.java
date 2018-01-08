package com.example.macstudio.instagramalison.api.services;

import com.example.macstudio.instagramalison.api.model.InstagramResponse;
import com.example.macstudio.instagramalison.api.model.MediaLikeResponse;

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
    Call<MediaLikeResponse> getMediaLikes(@Path("media-id") int mediaId,
                                          @Query("access_token") String access_token);

    @POST("v1/media/{media-id}/likes")
    Call<MediaLikeResponse> postLikeMedia();

    @DELETE("v1/media/{media-id}/likes")
    Call<MediaLikeResponse> deleteLikeMedia();

    //should finish the post and delete methods, then complete the MediaLikeResponse class
}
