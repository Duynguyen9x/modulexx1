package com.add.toeic.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.add.toeic.R;
import com.add.toeic.adapter.TabsPagerAdapter;
import com.add.toeic.fragment.TopicPracticeFragment;
import com.add.toeic.fragment.TopicWordFragment;

public class TopicItemActivity extends AppCompatActivity implements TopicWordFragment.OnFragmentInteractionListener,
        TopicPracticeFragment.OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_item);

        // Initilization
        String data = getIntent().getStringExtra("keyName");
        String sLocation = getIntent().getExtras().getString("location");
        int location = Integer.parseInt(sLocation);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(data);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this, location);

        //Set an Apater for the View Pager
        viewPager.setAdapter(mAdapter);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
