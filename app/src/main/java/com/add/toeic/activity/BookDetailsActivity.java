package com.add.toeic.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.add.toeic.R;

public class BookDetailsActivity extends AppCompatActivity {
    String mNumber, mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView view = (WebView) findViewById(R.id.wv_html);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setBuiltInZoomControls(true);
        view.getSettings().setSupportZoom(true);
        view.getSettings().setUseWideViewPort(false);
      //  view.setInitialScale(150);
        view.setBackgroundColor(Color.TRANSPARENT);

        //  view.loadUrl("file:///android_asset/book/1.htm");

        mNumber = getIntent().getStringExtra("number");
        mContent = getIntent().getStringExtra("content");

        view.loadUrl(loadUrl(mNumber));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bài " + mNumber + ":" + mContent);
    }

    private String loadUrl(String number) {
        return "file:///android_asset/book/" + number + ".htm";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
