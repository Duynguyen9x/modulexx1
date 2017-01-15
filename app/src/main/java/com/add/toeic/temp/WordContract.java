package com.add.toeic.temp;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DTA on 1/11/2017.
 */

public class WordContract {

    public static final String AUTHORITY = "com.add.toeic";

    public static class Word implements BaseColumns {

        // 2 tables
        public static final String TB_WORD_ALL = "words";
        public static final String TB_WORD_REMIND = "remind_words";


        public static final String URL = "content://" + AUTHORITY;

        public static final Uri CONTENT_URI_ALL = Uri.parse(URL).buildUpon().appendPath(TB_WORD_ALL).build();
        public static final Uri CONTENT_URI_REMIND = Uri.parse(URL).buildUpon().appendPath(TB_WORD_REMIND).build();

        public static final String ID = "id";

        public static final String NAME = "name";

        public static final String NAME_KEY = "name_key";

        public static final String SOUND = "sound";

        public static final String EXAMPLE = "example";

        public static final String EXAMPLE_KEY = "example_key";

        public static final String EXPAND = "expand";

        public static final String KIND = "kind";

        public static final String REMEMBER = "remember";

        public static Uri buildUri(Uri uri, long id) {
            return ContentUris.withAppendedId(uri, id);
        }
    }
}
