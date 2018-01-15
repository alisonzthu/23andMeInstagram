package com.example.macstudio.instagramalison.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.SelfLikeMediaResponse;
import com.example.macstudio.instagramalison.api.services.ServiceGenerator;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by azhang on 1/14/18.
 */

public class FeedCursorAdapter extends CursorAdapter {
    private static String access_token = null;
    private static final String TAG = FeedCursorAdapter.class.getSimpleName();

    public FeedCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d(TAG, "call many times");
        return LayoutInflater.from(context).inflate(
                R.layout.list_item_pagination, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String MEDIA_ID = cursor.getString(cursor.getColumnIndexOrThrow("mediaId"));
        // put all the layout elements down here?????!!!
        ImageView avatar = view.findViewById(R.id.avatar);
        String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow("avatar"));
        Picasso.with(context)
                .load(avatarUrl)
                .resize(100, 100)
                .centerInside()
                .into(avatar);

        TextView usernameText = view.findViewById(R.id.user_full_name);
        String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        usernameText.setText(username);

        ImageView feedImage = view.findViewById(R.id.feed_photo);
        String feedUrl = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        Picasso.with(context)
                .load(feedUrl)
                .into(feedImage);

        LikeButton likeButton = view.findViewById(R.id.like_button);
        final boolean user_has_liked = cursor.getInt(cursor.getColumnIndexOrThrow("likeStatus")) == 0 ? false : true;

        final TextView like_text = view.findViewById(R.id.like_text);

        final int likeCount = cursor.getInt(cursor.getColumnIndexOrThrow("likeCount"));

        if (user_has_liked) {
            likeButton.setLiked(true);
            int likeCountByOthers = likeCount - 1;
            updateLikeText(likeCountByOthers, like_text);
        } else {
            likeButton.setLiked(false);
            updateUnlikeCount(likeCount, like_text);
        }

        likeButton.setOnLikeListener(new OnLikeListener(){
            @Override
            public void liked(final LikeButton likeButton) {
                Log.d(TAG, "Making like feed request");
                // POST like
                Call<SelfLikeMediaResponse> call = ServiceGenerator.createLikeService().postLikeMedia(MEDIA_ID, access_token);
                call.enqueue(new Callback<SelfLikeMediaResponse>() {
                    @Override
                    public void onResponse(Call<SelfLikeMediaResponse> call, Response<SelfLikeMediaResponse> response) {
                        Log.i(TAG,"POST like successful");
                        // likeCount is the count before successful POST
                        // user_has_liked == true means user like at the beginning, then unlike -> like
                        int updatedLikeCount = user_has_liked ? likeCount - 1 : likeCount;
                        updateLikeText(updatedLikeCount, like_text);
                    }

                    @Override
                    public void onFailure(Call<SelfLikeMediaResponse> call, Throwable t) {
                        Log.e(TAG, "Post like failed: " + t.getMessage());
                        likeButton.setLiked(false);
                        Toast.makeText(context, "Failed to like", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void unLiked(final LikeButton likeButton) {
                Log.i(TAG,"Making unlike feed request");
                // DELETE like
                Call<Void> call = ServiceGenerator.createLikeService().deleteLikeMedia(MEDIA_ID, access_token);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.i(TAG,"DELETE like successful");
                        // user_has_liked == true means user doesn't like at the beginning, but then like -> unlike
                        int updatedLikeCount = user_has_liked ? likeCount - 1 : likeCount;
                        updateUnlikeCount(updatedLikeCount, like_text);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG,"DELETE like failed: " + t.getMessage());
                        likeButton.setLiked(true);
                        Toast.makeText(context, "Failed to unlike", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//
//        String likeTextText = cursor.getString(cursor.getColumnIndexOrThrow("likeText"));
//        likeText.setText(likeTextText);
    }

    public void updateLikeText(int likeCount, TextView like_text) {
        if (likeCount == 0) {
            like_text.setText("You like this pic");
        } else if (likeCount == 1) {
            like_text.setText("You and 1 other person like this pic");
        } else {
            like_text.setText("You and " + likeCount + " others like this pic");
        }
    }

    public void updateUnlikeCount(int likeCount, TextView like_text) {
        if (likeCount >= 2) {
            like_text.setText(likeCount + " others like this pic");
        }else if (likeCount == 1) {
            like_text.setText("1 person likes this pic");
        } else {
            like_text.setText("");
        }
    }
}
