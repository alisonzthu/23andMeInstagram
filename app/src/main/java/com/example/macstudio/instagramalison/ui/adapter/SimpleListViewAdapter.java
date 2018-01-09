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

import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.R;
import com.example.macstudio.instagramalison.api.model.InstagramResponse;
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
        View currentView = convertView;
        if (currentView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            currentView = vi.inflate(R.layout.list_item_pagination, null);
        }

        ImageView avatar = currentView.findViewById(R.id.avatar);
        TextView user_fullname = currentView.findViewById(R.id.user_fullname);
        ImageView feed_photo = currentView.findViewById(R.id.feed_photo);
        LikeButton like_button = currentView.findViewById(R.id.like_button);
        if (data.get(position).isUser_has_liked()) {
            like_button.setLiked(true);
        } else {
            like_button.setLiked(false);
        }

        Picasso.with(context)
                .load(data.get(position).getUser().getProfile_picture())
                .resize(100, 100)
                .centerInside()
                .into(avatar);

        user_fullname.setText(data.get(position).getUser().getFull_name());

        Picasso.with(context)
                .load(data.get(position).getImages().getStandard_resolution().getUrl())
                .into(feed_photo);

        // get the like button and set eventListener on it
        like_button.setOnLikeListener(new OnLikeListener(){
            @Override
            public void liked(LikeButton likeButton) {
                Log.d("making like request", likeButton.isLiked()+"");
                // make POST request
                Call<SelfLikeMediaResponse> call = ServiceGenerator.createLikeService().postLikeMedia(MEDIA_ID, access_token);
                call.enqueue(new Callback<SelfLikeMediaResponse>() {
                    @Override
                    public void onResponse(Call<SelfLikeMediaResponse> call, Response<SelfLikeMediaResponse> response) {
                        Log.d("post", "successful");
                    }

                    @Override
                    public void onFailure(Call<SelfLikeMediaResponse> call, Throwable t) {
                        Log.e("post", "failed");
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Log.d("making unlike request", likeButton.isLiked()+"");
                // make DELETE request
                Call<SelfLikeMediaResponse> call = ServiceGenerator.createLikeService().deleteLikeMedia(MEDIA_ID, access_token);
                call.enqueue(new Callback<SelfLikeMediaResponse>() {
                    @Override
                    public void onResponse(Call<SelfLikeMediaResponse> call, Response<SelfLikeMediaResponse> response) {
                        Log.d("delete", "successful");
                    }

                    @Override
                    public void onFailure(Call<SelfLikeMediaResponse> call, Throwable t) {
                        Log.d("delete", "failed");
                    }
                });
            }
        });
        return currentView;
    }

    public void clearListView() {
        data.clear();
    }
}
