package com.example.macstudio.instagramalison.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
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
import com.example.macstudio.instagramalison.api.services.SharedPrefManager;
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
    private Context context;
    private static final String TAG = FeedCursorAdapter.class.getSimpleName();

    public FeedCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
        this.context = context;
        SharedPreferences sharedPreferences= context.getSharedPreferences(SharedPrefManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        access_token = sharedPreferences.getString("access_token", "");
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
        // put all the layout elements for each media here
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
        // set up the like button status and like text:
        final boolean user_has_liked = cursor.getInt(cursor.getColumnIndexOrThrow("likeStatus")) != 0;
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
        // set up listener
        likeButton.setOnLikeListener(new OnLikeListener(){
            @Override
            public void liked(final LikeButton likeButton) {
                Log.d(TAG, "Making like feed request");
                // update text right after user clicks button
                // likeCount is the count before successful POST
                // user_has_liked == true means user like at the beginning, then unlike -> like
                final int updatedLikeCount = user_has_liked ? likeCount - 1 : likeCount;
                final String likeTextOriginal = like_text.getText().toString();
                updateLikeText(updatedLikeCount, like_text);
                // POST like
                Call<SelfLikeMediaResponse> call = ServiceGenerator.createLikeService().postLikeMedia(MEDIA_ID, access_token);
                call.enqueue(new Callback<SelfLikeMediaResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SelfLikeMediaResponse> call, @NonNull Response<SelfLikeMediaResponse> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG,"POST like successful");
                            // update db
                            updateDb(true, updatedLikeCount, MEDIA_ID);
                        } else {
                            Log.w(TAG, "POST like not successful");
                            Toast.makeText(context, "Post like failed!", Toast.LENGTH_SHORT).show();
                            like_text.setText(likeTextOriginal);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SelfLikeMediaResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Post like failed: " + t.getMessage());
                        likeButton.setLiked(false);
                        Toast.makeText(context, "Failed to like", Toast.LENGTH_SHORT).show();
                        like_text.setText(likeTextOriginal);
                    }
                });
            }

            @Override
            public void unLiked(final LikeButton likeButton) {
                Log.i(TAG,"Making unlike feed request");
                // user_has_liked == true means user doesn't like at the beginning, but then like -> unlike
                final int updatedLikeCount = user_has_liked ? likeCount - 1 : likeCount;
                final String likeTextOriginal = like_text.getText().toString();
                updateUnlikeCount(updatedLikeCount, like_text);
                // DELETE like
                Call<Void> call = ServiceGenerator.createLikeService().deleteLikeMedia(MEDIA_ID, access_token);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG,"DELETE like successful");
                            // update db
                            updateDb(false, updatedLikeCount, MEDIA_ID);
                        } else {
                            Log.w(TAG, "DELETE like not successful");
                            Toast.makeText(context, "DELETE like failed!", Toast.LENGTH_SHORT).show();
                            like_text.setText(likeTextOriginal);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e(TAG,"DELETE like failed: " + t.getMessage());
                        likeButton.setLiked(true);
                        like_text.setText(likeTextOriginal);
                        Toast.makeText(context, "Failed to unlike", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateLikeText(int likeCount, TextView like_text) {
        if (likeCount == 0) {
            like_text.setText(R.string.user_alone_like_this_pic);
        } else if (likeCount == 1) {
            like_text.setText(R.string.user_and_one_other_like_this_pic);
        } else {
            String msg = "You and " + likeCount + " others like this picture";
            like_text.setText(msg);
        }
    }

    private void updateUnlikeCount(int likeCount, TextView like_text) {
        if (likeCount >= 2) {
            String msg = likeCount + "others like this picture";
            like_text.setText(msg);
        }else if (likeCount == 1) {
            like_text.setText(R.string.one_other_likes_this_pic);
        } else {
            like_text.setText("");
        }
    }

    private void updateDb(boolean updatedLikeStatus, int updatedLikeCount, String mediaId) {
        FeedDbHelper feedDbHelper = new FeedDbHelper(this.context);
        SQLiteDatabase db = feedDbHelper.getWritableDatabase();

        int likeStatusInt = updatedLikeStatus ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(FeedContract.FeedEntry.COLUMN_NAME_LIKESTATUS, likeStatusInt);
        values.put(FeedContract.FeedEntry.COLUMN_NAME_LIKECOUNT, updatedLikeCount);
        db.update(
                FeedContract.FeedEntry.TABLE_NAME,
                values,
                FeedContract.FeedEntry.COLUMN_NAME_MEDIAID + "='" + mediaId + "'",
                null);
        Log.i(TAG, "DB updated");
    }
}
