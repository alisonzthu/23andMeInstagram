package com.example.macstudio.instagramalison.database;

import android.provider.BaseColumns;

/**
 * Created by azhang on 1/12/18.
 */

public class FeedContract {
    public static final String DB_NAME = "instagramFeed.db";
    public static final int DB_VERSION = 1;

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                FeedEntry._ID + " INTEGER PRIMARY KEY," +
                FeedEntry.COLUMN_NAME_USERNAME + " TEXT," +
                FeedEntry.COLUMN_NAME_AVATAR + " TEXT," +
                FeedEntry.COLUMN_NAME_IMAGE + " TEXT," +
                FeedEntry.COLUMN_NAME_LIKETEXT + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "feeds";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_LIKETEXT = "likeText";
    }
}
