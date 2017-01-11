package com.add.toeic.Constants;

import android.net.Uri;

/**
 * Created by DTA on 1/6/2017.
 */

public class DBInfo {

    // name and version
    public static final String DATABASE_NAME = "addtoeic.db";
    public static final int DATABASE_VERSION = 1;

    // 2 tables
    public static final String TABLE_WORD_REMIND = "word";
    public static final String TABLE_WORD_ALL = "all_words";

    // columns
    public static final String COLUMN_WORD_ID = "word_id";
    public static final String COLUMN_WORD_NAME = "word_name";
    public static final String COLUMN_WORD_NAME_KEY = "word_name_key";
    public static final String COLUMN_WORD_SOUND = "word_sound";
    public static final String COLUMN_WORD_EXAMPLE = "word_example";
    public static final String COLUMN_WORD_EXAMPLE_KEY = "word_example_key";
    public static final String COLUMN_WORD_KIND = "word_kind";

    public static final String AUTHORITY = "com.add.toeic";
    public static final String URL = "content://" + AUTHORITY;
    public static final Uri CONTENT_URI = Uri.parse(URL).buildUpon().appendPath(DBInfo.TABLE_WORD_REMIND).build();

}
