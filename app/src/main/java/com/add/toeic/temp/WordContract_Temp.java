package com.add.toeic.temp;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DTA on 1/11/2017.
 */

public class WordContract_Temp {

    public static final String AUTHORITY = "com.add.toeic";

    public static class Word implements BaseColumns {

        // 2 tables
        public static final String TABLE_WORD_REMIND = "word";
        public static final String TABLE_WORD_ALL = "all_words";

        public static final String URL = "content://" + AUTHORITY;

        public static final Uri CONTENT_URI_REMIND = Uri.parse(URL).buildUpon().appendPath(TABLE_WORD_REMIND).build();
        public static final Uri CONTENT_URI_ALL = Uri.parse(URL).buildUpon().appendPath(TABLE_WORD_ALL).build();

        public static final String WORD_ID = "word_id";

        public static final String WORD_NAME = "word_name";

        public static final String WORD_SOUND = "word_sound";

        public static final String WORD_NAME_KEY = "word_name_key";

        public static final String WORD_EXAMPLE = "word_example";

        public static final String WORD_EXAMPLE_KEY = "word_example_key";

        public static final String WORD_EXPLORE = "word_explore";

        public static final String WORD_KIND = "word_kind";

        public static final String WORD_STATUS = "status";

        public static Uri buildUriRemind(long id) {

            return ContentUris.withAppendedId(CONTENT_URI_REMIND, id);

        }

        public static Uri buildUriAll(long id) {

            return ContentUris.withAppendedId(CONTENT_URI_ALL, id);

        }
    }
}
