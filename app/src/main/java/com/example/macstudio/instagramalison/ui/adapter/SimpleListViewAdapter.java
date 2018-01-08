package com.example.macstudio.instagramalison.ui.adapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.R;
import com.like.LikeButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by macstudio on 12/29/17.
 */

public class SimpleListViewAdapter extends ArrayAdapter<InstagramData>{
    private Context context;
    private ArrayList<InstagramData> data;

    public SimpleListViewAdapter(Context context, int textViewResourceId, ArrayList<InstagramData> dataObjects){
        super(context, textViewResourceId, dataObjects);
        this.context = context;
        this.data = dataObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String IMAGE_ID = data.get(position).getId();
        View currentView = convertView;
        if (currentView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            currentView = vi.inflate(R.layout.list_item_pagination, null);
        }

        ImageView avatar = currentView.findViewById(R.id.avatar);
        TextView user_fullname = currentView.findViewById(R.id.user_fullname);
        ImageView feed_photo = currentView.findViewById(R.id.feed_photo);
        TextView like_count = currentView.findViewById(R.id.like_count);

        Picasso.with(context)
                .load(data.get(position).getUser().getProfile_picture())
                .resize(100, 100)
                .centerInside()
                .into(avatar);

        user_fullname.setText(data.get(position).getUser().getFull_name());

        Picasso.with(context)
                .load(data.get(position).getImages().getStandard_resolution().getUrl())
                .into(feed_photo);
// needs to be fixed. currently shows a string of things
//        like_count.setText(data.get(position).getLikes()+"");

        // get the like button and set eventListener on it
        LikeButton like_button = (LikeButton) currentView.findViewById(R.id.like_button);
        like_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("like button clicked", IMAGE_ID);
                // check the status of like button
                // if not liked, send POST
                // if liked, send DELETE
            }
        });
        return currentView;
    }

    public void clearListView() {
        data.clear();
    }
}
