package com.add.toeic.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by DTA on 1/26/2017.
 */

public class SearchProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.add.toeic.provider.SearchProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
