package info.androidhive.navigationdrawer.activity;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.model.Word;
import info.androidhive.navigationdrawer.utils.ImageUtils;
import info.androidhive.navigationdrawer.utils.WordUtils;
import info.androidhive.navigationdrawer.utils.ZoomOutPageTransformer;

public class DetailWordActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public Context mContext;
    Timer mTimer;
    private static int TIME = 2000;
    int curent_item = 0;
    boolean isRunning = false;
    private ArrayList<Word> arrWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_word);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String s_num_group = getIntent().getExtras().getString("num_group");
        int num_group = Integer.parseInt(s_num_group);

        String s_num_word = getIntent().getExtras().getString("num_word");
        int num_word = Integer.parseInt(s_num_word);

        Log.i("duy.pq", "DetailWordActivity=" + num_group + "=" + num_word);

        arrWord = getListData(num_group);

        for (int i = 0; i < arrWord.size(); i++) {
            Log.i("duy.pq", "arrWord" + arrWord.get(i).toString());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // set data
        mSectionsPagerAdapter.mGroupWord = num_word;
        mSectionsPagerAdapter.mWord = num_word;
        mSectionsPagerAdapter.mArrayList = arrWord;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isRunning) {
                    Snackbar.make(view, "Chạy từ tự động tạm dừng", Snackbar.LENGTH_LONG).show();
                    if (mTimer != null) {
                        mTimer.cancel();
                    }
                    isRunning = false;
                } else {
                    isRunning = false;
                    if (mTimer != null) {
                        mTimer.cancel();
                    }
                    Snackbar.make(view, "Chạy từ tự động", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).setAction("Bắt đầu", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Toast.makeText(mContext, "duy hand some", Toast.LENGTH_SHORT).show();
                            isRunning = true;
                            auto();
                        }
                    }).show();
                }
            }
        });
    }

    private ArrayList<Word> getListData(int grorp) {
        ArrayList<Word> arrAll = null;
        ArrayList<Word> arr = new ArrayList<Word>();

        try {
            arrAll = WordUtils.readAllData(mContext);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (arrAll == null)
            return null;
        for (int i = grorp * 12; i < (grorp * 12 + 12); i++) {
            arr.add(arrAll.get(i));
        }
        return arr;
    }


    public void auto() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        opentag();
                    }
                });
            }
        }, TIME, TIME);
    }

    public void opentag() {
        mViewPager.setCurrentItem(curent_item);
        if (curent_item == 11) {
            curent_item = 0;
        } else {
            curent_item++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        public static int mNum;
        private Context mContext;
        TextView tv1, tv2, tv3, tv_num;
        ImageButton mImageButtonVoice, mImageButtonSpeak;
        ImageView mImageWord;
        private static final int SPEECH_REQUEST_CODE = 0;
        public static ArrayList<Word> mListData;
        public static int mGroupWord, mWord;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, ArrayList<Word> arr, int group, int word) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            mListData = arr;
            mGroupWord = word;
            mWord = word;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_word, container, false);

            tv1 = (TextView) rootView.findViewById(R.id.tv_line_1);
            tv2 = (TextView) rootView.findViewById(R.id.tv_line_2);
            tv3 = (TextView) rootView.findViewById(R.id.tv_line_3);
            tv_num = (TextView) rootView.findViewById(R.id.tv_num);

            int num_pager = getArguments().getInt(ARG_SECTION_NUMBER);
            int num_sum = mListData.size();
            mContext = getActivity();
            tv_num.setText("(" + num_pager + "/" + num_sum + ")");
            mImageButtonVoice = (ImageButton) rootView.findViewById(R.id.image_google_voice);
            mImageButtonSpeak = (ImageButton) rootView.findViewById(R.id.image_speak);
            mImageWord = (ImageView) rootView.findViewById(R.id.image_word);
            mImageWord.setImageDrawable(ImageUtils.loadDrawableLocal(mContext, mListData.get(num_pager - 1).getName()));

            mImageButtonVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, SPEECH_REQUEST_CODE);
                }
            });

            //  tv1.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            tv1.setText(mListData.get(num_pager - 1).getName());
            tv2.setText(mListData.get(num_pager - 1).getSound());
            tv3.setText(mListData.get(num_pager - 1).getExamle());

//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {
            if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                String spokenText = results.get(0);
                tv2.setText(spokenText);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public int mGroupWord, mWord;
        public ArrayList<Word> mArrayList;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1, mArrayList, mGroupWord, mWord);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mArrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
