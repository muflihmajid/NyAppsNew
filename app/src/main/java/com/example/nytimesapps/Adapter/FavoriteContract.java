package com.example.nytimesapps.Adapter;

import android.provider.BaseColumns;

public class FavoriteContract {
    public static final class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NEWSID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "posterpath";
        public static final String COLUMN_WEB = "URL";
    }
}
