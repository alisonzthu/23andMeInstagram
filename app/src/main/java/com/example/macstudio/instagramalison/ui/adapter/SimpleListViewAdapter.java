package com.example.macstudio.instagramalison.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.SelfLikeMediaResponse;
import com.example.macstudio.instagramalison.api.services.ServiceGenerator;
import com.example.macstudio.instagramalison.api.services.SharedPrefManager;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by macstudio on 12/29/17.
 */

public class SimpleListViewAdapter extends ArrayAdapter<InstagramData>{
    private Context context;
    private ArrayList<InstagramData> data;
    private static String access_token = null;
    private static final String TAG = SimpleListViewAdapter.class.getSimpleName();


    public SimpleListViewAdapter(Context context, int textViewResourceId, ArrayList<InstagramData> dataObjects){
        super(context, textViewResourceId, dataObjects);
        this.context = context;
        this.data = dataObjects;
        // read from SharedPreferences for access_token
        SharedPreferences sharedPreferences= getContext().getSharedPreferences(SharedPrefManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        access_token = sharedPreferences.getString("access_token", "");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String MEDIA_ID = data.get(position).getId();
        Log.d("media id", MEDIA_ID);
        View currentView = convertView;
        if (currentView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            currentView = vi.inflate(R.layout.list_item_pagination, null);
        }

        ImageView avatar = currentView.findViewById(R.id.avatar);
        TextView user_full_name = currentView.findViewById(R.id.user_full_name);
        ImageView feed_photo = currentView.findViewById(R.id.feed_photo);
        final LikeButton like_button = currentView.findViewById(R.id.like_button);
        final TextView like_text = currentView.findViewById(R.id.like_text);
        final int likeCount = data.get(position).getLikes().getCount();
        final boolean user_has_liked = data.get(position).isUser_has_liked();

        if (user_has_liked) {
            like_button.setLiked(true);
            int likeCountByOthers = likeCount - 1;
            updateLikeText(likeCountByOthers, like_text);
        } else {
            like_button.setLiked(false);
            updateUnlikeCount(likeCount, like_text);
        }

        Picasso.with(context)
                .load(data.get(position).getUser().getProfile_picture())
                .resize(100, 100)
                .centerInside()
                .into(avatar);

        user_full_name.setText(data.get(position).getUser().getFull_name());

        Picasso.with(context)
                .load(data.get(position).getImages().getStandard_resolution().getUrl())
                .into(feed_photo);

        // get the like button and set eventListener on it
        like_button.setOnLikeListener(new OnLikeListener(){
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
                        Toast.makeText(getContext(), "Failed to like", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Failed to unlike", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return currentView;
    }

    // I'm not clearing listview content. Why and when should I do it?
//    public void clearListView() {
//        data.clear();
//    }

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
