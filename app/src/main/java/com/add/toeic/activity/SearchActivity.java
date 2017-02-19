package com.add.toeic.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.provider.AppProvider;
import com.add.toeic.provider.SearchProvider;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private Toolbar mToolbar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_search);
        Log.d("anhdt", TAG + " onCreate, getIntent = " + getIntent().getAction());
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("anhdt", TAG + " onNewIntent, intent = " + intent.getAction());
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d("anhdt", TAG + " handleIntent, intent = " + intent.getAction());
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        Toast.makeText(getApplicationContext(), "doMySearch : " + query, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        // Get the SearchView and set the searchable configuration
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItemCompat.expandActionView(searchMenuItem);
        // can not set full width in xml
        searchView.setMaxWidth(Integer.MAX_VALUE);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                Log.d("anhdt", TAG + ", onBackPress");
                finish();
                // avoid blink toolbar when finish current activity
                overridePendingTransition(0, 0);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("anhdt", TAG + ", onQueryTextSubmit " + query);
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(mContext, SearchProvider.AUTHORITY, SearchProvider.MODE);
                suggestions.saveRecentQuery(query, null);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("anhdt", TAG + ", onQueryTextChange " + newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_all_history) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchProvider.AUTHORITY, SearchProvider.MODE);
            suggestions.clearHistory();
        }
        return super.onOptionsItemSelected(item);
    }
}