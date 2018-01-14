package com.example.macstudio.instagramalison.database;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by azhang on 1/14/18.
 */

public class FeedCursorAdapter extends CursorAdapter {
    public FeedCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
