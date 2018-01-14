package com.example.macstudio.instagramalison.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by azhang on 1/12/18.
 */

public class FeedDbHelper extends SQLiteOpenHelper {
    public FeedDbHelper(Context context) {
        super(context, FeedContract.DB_NAME, null, FeedContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(FeedContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
