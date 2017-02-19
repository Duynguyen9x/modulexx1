package com.add.toeic.activity;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.adapter.BookTheoryAdapter;
import com.add.toeic.model.BookTheory;
import com.add.toeic.temp.PlayerConfig;
import com.add.toeic.utils.WordUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    Context mContext;
    private ArrayList<BookTheory> mArrList = null;
    private BookTheoryAdapter mViewAdapter = null;
    private ListView mListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        mContext = this;
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        mListView = (ListView) findViewById(R.id.lv_video);
        mArrList = new ArrayList<>();

        mViewAdapter = new BookTheoryAdapter(mContext, R.layout.booktheory_item_layout, mArrList);
        mListView.setAdapter(mViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "YouTubeBaseActivity ", Toast.LENGTH_SHORT).show();
            }
        });
        initLoadData();

        youTubePlayerView.initialize(PlayerConfig.API_KEY, this);
    }

    public void initLoadData() {
        AsyncTask<Void, Void, ArrayList<BookTheory>> loadBitmapTask = new AsyncTask<Void, Void, ArrayList<BookTheory>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<BookTheory> doInBackground(Void... params) {
                return getListBooks();
            }

            @Override
            protected void onPostExecute(ArrayList<BookTheory> listBooks) {
                mArrList = listBooks;
                mViewAdapter.addAll(listBooks);
                mViewAdapter.notifyDataSetChanged();
                mListView.setAdapter(mViewAdapter);
                super.onPostExecute(listBooks);
            }
        };

        loadBitmapTask.execute();
    }

    public ArrayList<BookTheory> getListBooks() {

        ArrayList<BookTheory> listBook = new ArrayList<>();
        List<String> name = Arrays.asList(mContext.getResources().getStringArray(R.array.meo_thi_part));

        for (int i = 0; i < name.size(); i++) {
            BookTheory book = new BookTheory();
            book.setNumber(String.valueOf(i + 1));
            book.setContent(name.get(i));
            listBook.add(book);
        }
        return listBook;

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.loadVideo("3LiubyYpEUk");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("There was an error initializing Youtube (%1$s)", errorReason.toString());
            Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYoutubePlayerProvider().initialize(PlayerConfig.API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player_view);
    }
}
