package com.example.macstudio.instagramalison.ui.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macstudio.instagramalison.api.model.InstagramData;
import com.example.macstudio.instagramalison.R;
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
        View currentView = convertView;
        if (currentView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            currentView = vi.inflate(R.layout.list_item_pagination, null);
        }

        ImageView avatar = currentView.findViewById(R.id.avatar);
        TextView user_fullname = currentView.findViewById(R.id.user_fullname);
        ImageView feed_photo = currentView.findViewById(R.id.feed_photo);

        Picasso.with(context)
                .load(data.get(position).getUser().getProfile_picture())
                .resize(100, 100)
                .centerInside()
                .into(avatar);

        user_fullname.setText(data.get(position).getUser().getFull_name());

        Picasso.with(context)
                .load(data.get(position).getImages().getStandard_resolution().getUrl())
                .into(feed_photo);
        return currentView;
    }

    public void clearListView() {
        data.clear();
    }
}
