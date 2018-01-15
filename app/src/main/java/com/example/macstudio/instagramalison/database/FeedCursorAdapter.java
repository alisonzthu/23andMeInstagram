package com.example.macstudio.instagramalison.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macstudio.instagramalison.R;
import com.like.LikeButton;
import com.squareup.picasso.Picasso;

/**
 * Created by azhang on 1/14/18.
 */

public class FeedCursorAdapter extends CursorAdapter {
    public FeedCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.list_item_pagination, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
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
        if (cursor.getInt(cursor.getColumnIndexOrThrow("likeStatus")) == 0) {
            likeButton.setLiked(false);
        } else {
            likeButton.setLiked(true);
        }

//        TextView likeText = view.findViewById(R.id.like_text);
//        String likeTextText = cursor.getString(cursor.getColumnIndexOrThrow("likeText"));
//        likeText.setText(likeTextText);
    }
}
