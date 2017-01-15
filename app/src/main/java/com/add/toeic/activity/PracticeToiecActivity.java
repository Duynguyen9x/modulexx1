package com.add.toeic.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.model.practice.ListenLong;
import com.add.toeic.model.practice.ListenShort;
import com.add.toeic.model.practice.QuestionResponse;
import com.add.toeic.model.practice.ReadCompletion;
import com.add.toeic.model.practice.ReadComprehension;
import com.add.toeic.model.practice.ReadSentence;
import com.add.toeic.utils.ImageUtils;
import com.add.toeic.utils.TimeUtils;
import com.add.toeic.utils.json.JsonListenLongUtils;
import com.add.toeic.utils.json.JsonListenShortUtils;
import com.add.toeic.utils.json.JsonQuestionResponseUtils;
import com.add.toeic.utils.json.JsonReadComplehensionUtils;
import com.add.toeic.utils.json.JsonReadCompletionUtils;
import com.add.toeic.utils.json.JsonReadSentenceUtils;
import com.bumptech.glide.Glide;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PracticeToiecActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private Context mContext;
    private ImageView imageView;
    private SeekBar mProgressBar;
    ArrayList<ListenShort> mListObj_part1;
    ArrayList<QuestionResponse> mListObj_part2;
    ArrayList<ListenLong> mListObj_part3;
    ArrayList<ListenLong> mListObj_part4;
    ArrayList<ReadSentence> mListObj_part5;
    ArrayList<ReadCompletion> mListObj_part6;
    ArrayList<ReadComprehension> mListObj_part7;
    ImageButton btn_play, btn_back, btn_next, btn_previous;
    Button btn_check;
    TextView tv_title, tv_cur, tv_num, tv_current_duration, tv_total_duration, tv_part2_description;
    int position_sentence_1, position_sentence_2, position_sentence_3, position_sentence_4, position_sentence_5, position_sentence_6, position_sentence_7, option_selected;
    RadioGroup ra_group;
    RadioButton ra_a, ra_b, ra_c, ra_d;

    MediaPlayer mediaPlayer;
    LayoutInflater inflater;
    private static boolean mIsCheck = false;

    // object for part 3
    RadioGroup ra_group1, ra_group2, ra_group3;
    RadioButton ra_a_1, ra_b_1, ra_c_1, ra_d_1, ra_a_2, ra_b_2, ra_c_2, ra_d_2, ra_a_3, ra_b_3, ra_c_3, ra_d_3;
    int option_selected_1, option_selected_2, option_selected_3;
    TextView tv_paragraph, tv_question1, tv_question2, tv_question3;

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    TimeUtils utils;
    int part_number;

    private static final String PATH_FILE_MP3 = "mp3practice/";
    private static final String PATH_FILE_MP3_1 = "mp3practice1/";
    private static final String DUOI_FILE = ".mp3";

    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_toiec);
        mContext = this;
        option_selected = 0;

        sharedPreference = new SharedPreference();

        part_number = getIntent().getIntExtra("partNum", 0);

        switch (part_number) {
            // view for part 1 & 2
            case 0:
                position_sentence_1 = sharedPreference.getValue(mContext);
                initActionBar();
                initView_part1();
                initControl_part1();
                showSentence_part12(position_sentence_1);
                resetSentence_part12();
                break;
            case 1:
                position_sentence_2 = sharedPreference.getValue(mContext);
                initActionBar();
                initView_part2();
                initControl_part2();
                showSentence_part12(position_sentence_2);
                resetSentence_part12();
                break;
            case 2:
                position_sentence_3 = sharedPreference.getValue(mContext);
                initActionBar();
                initView_part3();
                initControl_part3();
                showSentence_part3(position_sentence_3);
                resetSentence_part3();
                break;
            case 3:
                position_sentence_4 = sharedPreference.getValue(mContext);
                initActionBar();
                initView_part3();
                initControl_part3();
                showSentence_part3(position_sentence_4);
                resetSentence_part3();
                break;
            case 4:
                position_sentence_5 = sharedPreference.getValue(mContext);
                initActionBar();
                initView_part5();
                initControl_part5();
                showSentence_part5(position_sentence_5);
                resetSentence_part5();
                break;
            case 5:
                position_sentence_6 = sharedPreference.getValue(mContext);
                initActionBar();
                initView_part3();
                initControl_part3();
                showSentence_part3(position_sentence_6);
                resetSentence_part3();
                break;
            case 6:
                position_sentence_7 = sharedPreference.getValue(mContext);
                initActionBar();
                initView_part3();
                initControl_part3();
                showSentence_part3(position_sentence_7);
                resetSentence_part3();
                break;
        }
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_front);

        View view = getSupportActionBar().getCustomView();

        String title = getIntent().getStringExtra("keyTitle");

        tv_title = (TextView) view.findViewById(R.id.tv_part_num);
        btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        btn_next = (ImageButton) view.findViewById(R.id.btn_next);
        btn_previous = (ImageButton) view.findViewById(R.id.btn_previous);
        tv_cur = (TextView) view.findViewById(R.id.tv_num_cur);
        tv_num = (TextView) view.findViewById(R.id.tv_num_total);

        tv_title.setText(title);
    }

    private void initBelowLayout() {
        View includedLayout = findViewById(R.id.custom_layout_below);
        btn_play = (ImageButton) includedLayout.findViewById(R.id.btn_play);
        btn_check = (Button) includedLayout.findViewById(R.id.btn_check);
        mProgressBar = (SeekBar) includedLayout.findViewById(R.id.progressBar);
        tv_current_duration = (TextView) includedLayout.findViewById(R.id.tv_currentDuration);
        tv_total_duration = (TextView) includedLayout.findViewById(R.id.tv_totalDuration);
    }

    private void initView_part1() {
        ra_group = (RadioGroup) findViewById(R.id.grp_answer);
        ra_a = (RadioButton) findViewById(R.id.rad_a);
        ra_b = (RadioButton) findViewById(R.id.rad_b);
        ra_c = (RadioButton) findViewById(R.id.rad_c);
        ra_d = (RadioButton) findViewById(R.id.rad_d);

        ra_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rad_a:
                        option_selected = 0;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_b:
                        option_selected = 1;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_c:
                        option_selected = 2;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_d:
                        option_selected = 3;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    default:
                        break;
                }
            }
        });

        imageView = (ImageView) findViewById(R.id.img_description);

        initBelowLayout();

        try {
            mListObj_part1 = JsonListenShortUtils.readerOjectFromJson(this, "l1_photo.json");
            Log.i("duy.pq", "item=" + mListObj_part1.get(0).toString());

            for (int i = 0; i < mListObj_part1.size(); i++) {
                Log.i("duy.pq", "PracticeToiecActivity.item=" + mListObj_part1.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView_part2() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(mContext);

        FrameLayout content_practice_toeic = (FrameLayout) findViewById(R.id.content_practice_toeic_part1);
        content_practice_toeic.removeAllViews();

        View content_practice_toeic_2 = inflater.inflate(R.layout.content_practice_toeic_part2, null);
        content_practice_toeic.addView(content_practice_toeic_2);

        content_practice_toeic.setPadding(10, 10, 10, 10);

        ra_group = (RadioGroup) findViewById(R.id.grp_answer);
        ra_a = (RadioButton) findViewById(R.id.rad_a);
        ra_b = (RadioButton) findViewById(R.id.rad_b);
        ra_c = (RadioButton) findViewById(R.id.rad_c);
        ra_d = (RadioButton) findViewById(R.id.rad_d);

        ra_d.setVisibility(View.GONE);

        ra_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rad_a:
                        option_selected = 0;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_b:
                        option_selected = 1;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_c:
                        option_selected = 2;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_d:
                        option_selected = 3;
                        setOptionsColor(option_selected, ra_group);
                    default:
                        break;
                }
            }
        });

        initBelowLayout();

        try {
            mListObj_part2 = JsonQuestionResponseUtils.readerOjectFromJson(this, "l1_question_response.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView_part3() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(mContext);

        FrameLayout content_practice_toeic_3 = (FrameLayout) findViewById(R.id.content_practice_toeic_part1);
        content_practice_toeic_3.removeAllViews();

        View content_practice_3;
        if (part_number == 5 || part_number == 6) {
            content_practice_3 = inflater.inflate(R.layout.srcoll_view_pratice_toeic_reading_comprehension, null);
            tv_paragraph = (TextView) content_practice_3.findViewById(R.id.tv_paragraph);
        } else {
            content_practice_3 = inflater.inflate(R.layout.srcoll_view_pratice_toeic, null);
        }

        content_practice_toeic_3.addView(content_practice_3);
        content_practice_toeic_3.setPadding(40, 40, 40, 460);

        tv_question1 = (TextView) findViewById(R.id.tv_question_1);
        tv_question2 = (TextView) findViewById(R.id.tv_question_2);
        tv_question3 = (TextView) findViewById(R.id.tv_question_3);

        ra_group1 = (RadioGroup) findViewById(R.id.radioGroup1);
        ra_a_1 = (RadioButton) findViewById(R.id.rbtn_a_1);
        ra_b_1 = (RadioButton) findViewById(R.id.rbtn_b_1);
        ra_c_1 = (RadioButton) findViewById(R.id.rbtn_c_1);
        ra_d_1 = (RadioButton) findViewById(R.id.rbtn_d_1);

        ra_group2 = (RadioGroup) findViewById(R.id.radioGroup2);
        ra_a_2 = (RadioButton) findViewById(R.id.rbtn_a_2);
        ra_b_2 = (RadioButton) findViewById(R.id.rbtn_b_2);
        ra_c_2 = (RadioButton) findViewById(R.id.rbtn_c_2);
        ra_d_2 = (RadioButton) findViewById(R.id.rbtn_d_2);

        ra_group3 = (RadioGroup) findViewById(R.id.radioGroup3);
        ra_a_3 = (RadioButton) findViewById(R.id.rbtn_a_3);
        ra_b_3 = (RadioButton) findViewById(R.id.rbtn_b_3);
        ra_c_3 = (RadioButton) findViewById(R.id.rbtn_c_3);
        ra_d_3 = (RadioButton) findViewById(R.id.rbtn_d_3);

        ra_group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_a_1:
                        option_selected_1 = 0;
                        setOptionsColor(0, ra_group1);
                        break;
                    case R.id.rbtn_b_1:
                        option_selected_1 = 1;
                        setOptionsColor(1, ra_group1);
                        break;
                    case R.id.rbtn_c_1:
                        option_selected_1 = 2;
                        setOptionsColor(2, ra_group1);
                        break;
                    case R.id.rbtn_d_1:
                        option_selected_1 = 3;
                        setOptionsColor(3, ra_group1);
                        break;
                    default:
                        break;
                }
            }
        });

        ra_group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_a_2:
                        option_selected_2 = 0;
                        setOptionsColor(0, ra_group2);
                        break;
                    case R.id.rbtn_b_2:
                        option_selected_2 = 1;
                        setOptionsColor(1, ra_group2);
                        break;
                    case R.id.rbtn_c_2:
                        option_selected_2 = 2;
                        setOptionsColor(2, ra_group2);
                        break;
                    case R.id.rbtn_d_2:
                        option_selected_2 = 3;
                        setOptionsColor(3, ra_group2);
                        break;
                    default:
                        break;
                }
            }
        });

        ra_group3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_a_3:
                        option_selected_3 = 0;
                        setOptionsColor(0, ra_group3);
                        break;
                    case R.id.rbtn_b_3:
                        option_selected_3 = 1;
                        setOptionsColor(1, ra_group3);
                        break;
                    case R.id.rbtn_c_3:
                        option_selected_3 = 2;
                        setOptionsColor(2, ra_group3);
                        break;
                    case R.id.rbtn_d_3:
                        option_selected_3 = 3;
                        setOptionsColor(3, ra_group3);
                        break;
                    default:
                        break;
                }
            }
        });

        initBelowLayout();

        if (part_number == 5 || part_number == 6) {
            btn_play.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            tv_current_duration.setVisibility(View.GONE);
            tv_total_duration.setVisibility(View.GONE);
        }

        try {
            if (part_number == 2)
                mListObj_part3 = JsonListenLongUtils.readerOjectFromJson(this, "l1_short_talk.json");
            else if (part_number == 3) {
                mListObj_part4 = JsonListenLongUtils.readerOjectFromJson(this, "l1_short_conversation.json");
            } else if (part_number == 5) {
                mListObj_part6 = JsonReadCompletionUtils.readerOjectFromJson(this, "l1_text_completion.json");
                for (int i=0;i<5;i++){
                    Log.i("duy.pq", "item5=" + mListObj_part6.get(i).toString());
                }
            }
            else
                mListObj_part7 = JsonReadComplehensionUtils.readerOjectFromJson(this, "l1_reading_comprehension.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView_part5() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(mContext);

        FrameLayout content_practice_toeic = (FrameLayout) findViewById(R.id.content_practice_toeic_part1);
        content_practice_toeic.removeAllViews();

        View content_practice_toeic_4 = inflater.inflate(R.layout.content_practice_toeic_part4, null);
        content_practice_toeic.addView(content_practice_toeic_4);

        content_practice_toeic.setPadding(10, 10, 10, 10);

        ra_group = (RadioGroup) findViewById(R.id.grp_answer);
        ra_a = (RadioButton) findViewById(R.id.rad_a);
        ra_b = (RadioButton) findViewById(R.id.rad_b);
        ra_c = (RadioButton) findViewById(R.id.rad_c);
        ra_d = (RadioButton) findViewById(R.id.rad_d);

        ra_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rad_a:
                        option_selected = 0;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_b:
                        option_selected = 1;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_c:
                        option_selected = 2;
                        setOptionsColor(option_selected, ra_group);
                        break;
                    case R.id.rad_d:
                        option_selected = 3;
                        setOptionsColor(option_selected, ra_group);
                    default:
                        break;
                }
            }
        });

        tv_part2_description = (TextView) findViewById(R.id.tv_description);

        initBelowLayout();

        if (part_number == 4) {
            btn_play.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            tv_current_duration.setVisibility(View.GONE);
            tv_total_duration.setVisibility(View.GONE);
        }

        try {
            mListObj_part5 = JsonReadSentenceUtils.readerOjectFromJson(this, "l1_sentences.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initControl_part1() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for already playing
                if(mediaPlayer.isPlaying()){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();

                        // Changing button image to play button
                        btn_play.setImageDrawable(getResources().getDrawable(R.drawable.action_play, mContext.getTheme()));
                    }
                }else{
                    // Resume song
                    if(mediaPlayer!=null){
//                        String SDCardRoot = Environment.getExternalStorageDirectory()
//                                .toString();
//                        String audioFilePath = SDCardRoot + "/MyAudioFolder/hosannatelugu.mp3";
////                        MediaPlayer mPlayer = new MediaPlayer();
//                        try {
//                            mediaPlayer.setDataSource(audioFilePath);
//                            mediaPlayer.prepare();
//                            mediaPlayer.start();
//                        } catch (IOException e) {
//                            Log.e("AUDIO PLAYBACK", "prepare() failed");
//                        }
                        playSong(position_sentence_1);
                        mediaPlayer.start();

                        // Changing button image to pause button
                        btn_play.setImageResource(R.drawable.action_pause);
                    }
                }
            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsCheck) {
                    showResult_part1(position_sentence_1);
                    mIsCheck = true;
                    btn_check.setText("Next");
                } else {
                    position_sentence_1++;
                    if (position_sentence_1 == mListObj_part1.size()) {
                        position_sentence_1 = 0;
                    }

                    sharedPreference.save(mContext, position_sentence_1);
                    resetSentence_part12();
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                    showSentence_part12(position_sentence_1);

                    btn_check.setText("Kiem tra");
                    mIsCheck = false;
                }

            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_check.setText("Kiem tra");
                mIsCheck = false;
                resetSentence_part12();
                position_sentence_1--;
                if (position_sentence_1 == -1) {
                    position_sentence_1 = mListObj_part1.size() - 1;
                }

                sharedPreference.save(mContext, position_sentence_1);
                mediaPlayer.pause();
                mediaPlayer.stop();
                showSentence_part12(position_sentence_1);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_check.setText("Kiem tra");
                mIsCheck = false;
                position_sentence_1++;
                if (position_sentence_1 == mListObj_part1.size()) {
                    position_sentence_1 = 0;
                }

                sharedPreference.save(mContext, position_sentence_1);
                resetSentence_part12();
                mediaPlayer.pause();
                mediaPlayer.stop();
                showSentence_part12(position_sentence_1);
            }
        });

    }

    private void initControl_part2() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for already playing
                if(mediaPlayer.isPlaying()){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();

                        // Changing button image to play button
                        btn_play.setImageDrawable(getResources().getDrawable(R.drawable.action_play, mContext.getTheme()));
                    }
                }else{
                    // Resume song
                    if(mediaPlayer!=null){
                        playSong(position_sentence_2);
                        mediaPlayer.start();

                        // Changing button image to pause button
                        btn_play.setImageResource(R.drawable.action_pause);
                    }
                }
            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsCheck) {
                    showResult_part2(position_sentence_2);
                    btn_check.setText("Next");
                    mIsCheck = true;
                } else {
                    position_sentence_2++;
                    if (position_sentence_2 == mListObj_part2.size()) {
                        position_sentence_2 = 0;
                    }

                    sharedPreference.save(mContext, position_sentence_2);
                    resetSentence_part12();
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                    showSentence_part12(position_sentence_2);

                    mIsCheck = false;
                    btn_check.setText("Kiem tra");
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheck = false;
                btn_check.setText("Kiem tra");
                resetSentence_part12();
                position_sentence_2--;
                if (position_sentence_2 == -1) {
                    position_sentence_2 = mListObj_part2.size() - 1;
                }

                sharedPreference.save(mContext, position_sentence_2);
                mediaPlayer.pause();
                mediaPlayer.stop();
                showSentence_part12(position_sentence_2);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheck = false;
                btn_check.setText("Kiem tra");

                position_sentence_2++;
                if (position_sentence_2 == mListObj_part2.size()) {
                    position_sentence_2 = 0;
                }

                sharedPreference.save(mContext, position_sentence_2);
                resetSentence_part12();
                mediaPlayer.pause();
                mediaPlayer.stop();
                showSentence_part12(position_sentence_2);
            }
        });

    }

    private void initControl_part3() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (part_number == 2) {
            btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check for already playing
                    if (mediaPlayer.isPlaying()) {
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();

                            // Changing button image to play button
                            btn_play.setImageDrawable(getResources().getDrawable(R.drawable.action_play, mContext.getTheme()));
                        }
                    } else {
                        // Resume song
                        if (mediaPlayer != null) {
                            playSong(position_sentence_3);
                            mediaPlayer.start();

                            // Changing button image to pause button
                            btn_play.setImageResource(R.drawable.action_pause);
                        }
                    }
                }
            });

            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsCheck) {
                        showResult_part3(position_sentence_3);
                        mIsCheck = true;
                        btn_check.setText("Next");
                    } else {
                        position_sentence_3++;

                        if (position_sentence_3 == mListObj_part3.size()) {
                            position_sentence_3 = 0;
                        }

                        mediaPlayer.pause();
                        mediaPlayer.stop();

                        sharedPreference.save(mContext, position_sentence_3);
                        resetSentence_part3();
                        showSentence_part3(position_sentence_3);

                        mIsCheck = false;
                        btn_check.setText("Kiem tra");
                    }
                }
            });

            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIsCheck = false;
                    btn_check.setText("Kiem tra");

                    position_sentence_3--;
                    if (position_sentence_3 == -1) {
                        position_sentence_3 = mListObj_part3.size() - 1;
                    }

                    mediaPlayer.pause();
                    mediaPlayer.stop();

                    sharedPreference.save(mContext, position_sentence_3);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_3);
                }
            });

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIsCheck = false;
                    btn_check.setText("Kiem tra");

                    position_sentence_3++;

                    if (position_sentence_3 == mListObj_part3.size()) {
                        position_sentence_3 = 0;
                    }

                    mediaPlayer.pause();
                    mediaPlayer.stop();

                    sharedPreference.save(mContext, position_sentence_3);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_3);
                }
            });
        } else if (part_number == 3) {
            btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check for already playing
                    if (mediaPlayer.isPlaying()) {
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();

                            // Changing button image to play button
                            btn_play.setImageDrawable(getResources().getDrawable(R.drawable.action_play, mContext.getTheme()));
                        }
                    } else {
                        // Resume song
                        if (mediaPlayer != null) {
                            playSong(position_sentence_4);
                            mediaPlayer.start();

                            // Changing button image to pause button
                            btn_play.setImageResource(R.drawable.action_pause);
                        }
                    }
                }
            });

            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsCheck) {
                        showResult_part3(position_sentence_4);

                        btn_check.setText("Next");
                        mIsCheck = true;
                    } else {
                        position_sentence_4++;

                        if (position_sentence_4 == mListObj_part4.size()) {
                            position_sentence_4 = 0;
                        }

                        mediaPlayer.pause();
                        mediaPlayer.stop();

                        sharedPreference.save(mContext, position_sentence_4);
                        resetSentence_part3();
                        showSentence_part3(position_sentence_4);

                        btn_check.setText("Kiem tra");
                        mIsCheck = false;
                    }
                }
            });

            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_check.setText("Kiem tra");
                    mIsCheck = false;

                    position_sentence_4--;
                    if (position_sentence_4 == -1) {
                        position_sentence_4 = mListObj_part4.size() - 1;
                    }

                    mediaPlayer.pause();
                    mediaPlayer.stop();

                    sharedPreference.save(mContext, position_sentence_4);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_4);
                }
            });

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_check.setText("Kiem tra");
                    mIsCheck = false;

                    position_sentence_4++;

                    if (position_sentence_4 == mListObj_part4.size()) {
                        position_sentence_4 = 0;
                    }

                    mediaPlayer.pause();
                    mediaPlayer.stop();

                    sharedPreference.save(mContext, position_sentence_4);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_4);
                }
            });
        } else if (part_number == 5) {
            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsCheck) {
                        showResult_part3(position_sentence_6);

                        btn_check.setText("Next");
                        mIsCheck = true;
                    } else {
                        position_sentence_6++;

                        if (position_sentence_6 == mListObj_part6.size()) {
                            position_sentence_6 = 0;
                        }

                        sharedPreference.save(mContext, position_sentence_6);
                        resetSentence_part3();
                        showSentence_part3(position_sentence_6);

                        btn_check.setText("Kiem tra");
                        mIsCheck = false;
                    }
                }
            });

            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_check.setText("Kiem tra");
                    mIsCheck = false;

                    position_sentence_6--;
                    if (position_sentence_6 == -1) {
                        position_sentence_6 = mListObj_part6.size() - 1;
                    }

                    sharedPreference.save(mContext, position_sentence_6);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_6);
                }
            });

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_check.setText("Kiem tra");
                    mIsCheck = false;

                    position_sentence_6++;

                    if (position_sentence_6 == mListObj_part6.size()) {
                        position_sentence_6 = 0;
                    }

                    sharedPreference.save(mContext, position_sentence_6);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_6);
                }
            });
        } else if (part_number == 6) {
            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsCheck) {
                        showResult_part3(position_sentence_7);

                        btn_check.setText("Next");
                        mIsCheck = true;
                    } else {
                        position_sentence_7++;

                        if (position_sentence_7 == mListObj_part7.size()) {
                            position_sentence_7 = 0;
                        }

                        sharedPreference.save(mContext, position_sentence_7);
                        resetSentence_part3();
                        showSentence_part3(position_sentence_7);

                        btn_check.setText("Kiem tra");
                        mIsCheck = false;
                    }
                }
            });

            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_check.setText("Kiem tra");
                    mIsCheck = false;

                    position_sentence_7--;
                    if (position_sentence_7 == -1) {
                        position_sentence_7 = mListObj_part7.size() - 1;
                    }

                    sharedPreference.save(mContext, position_sentence_7);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_7);
                }
            });

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_check.setText("Kiem tra");
                    mIsCheck = false;

                    position_sentence_7++;
                    if (position_sentence_7 == mListObj_part7.size()) {
                        position_sentence_7 = 0;
                    }

                    sharedPreference.save(mContext, position_sentence_7);
                    resetSentence_part3();
                    showSentence_part3(position_sentence_7);
                }
            });
        }
    }

    private void initControl_part5() {
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsCheck) {
                    showResult_part4(position_sentence_5);

                    btn_check.setText("Next");
                    mIsCheck = true;
                } else {
                    position_sentence_5++;
                    if (position_sentence_5 == mListObj_part5.size()) {
                        position_sentence_5 = 0;
                    }

                    sharedPreference.save(mContext, position_sentence_5);
                    resetSentence_part5();
                    showSentence_part5(position_sentence_5);

                    btn_check.setText("Kiem tra");
                    mIsCheck = false;
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_check.setText("Kiem tra");
                mIsCheck = false;

                resetSentence_part5();
                position_sentence_5--;
                if (position_sentence_5 == -1) {
                    position_sentence_5 = mListObj_part5.size() - 1;
                }

                sharedPreference.save(mContext, position_sentence_5);
                showSentence_part5(position_sentence_5);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_check.setText("Kiem tra");
                mIsCheck = false;

                position_sentence_5++;
                if (position_sentence_5 == mListObj_part5.size()) {
                    position_sentence_5 = 0;
                }

                sharedPreference.save(mContext, position_sentence_5);
                resetSentence_part5();
                showSentence_part5(position_sentence_5);
            }
        });
    }

    private void showResult_part1(int position) {
        if (position < 0 || position >= mListObj_part1.size()) {
            return;
        }
        String[] s = mListObj_part1.get(position_sentence_1).getOptions();
        int answer = mListObj_part1.get(position_sentence_1).getAnswer();
        ra_a.setText("A : " + s[0]);
        ra_b.setText("B : " + s[1]);
        ra_c.setText("C : " + s[2]);
        ra_d.setText("D : " + s[3]);

        ((RadioButton) ra_group.getChildAt(answer)).setTextColor(mContext.getResources().getColor(R.color.colorRed));
        if (answer == option_selected) {
            Toast.makeText(getApplicationContext(), "You Right!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "You Wrong!", Toast.LENGTH_SHORT).show();
        }

        setEnableRadioButton(ra_group, false);
    }

    private void showResult_part2(int position) {
        if (position < 0 || position >= mListObj_part2.size()) {
            return;
        }
        String[] s = mListObj_part2.get(position_sentence_2).getOptions();
        int answer = mListObj_part2.get(position_sentence_2).getAnswer();
        ra_a.setText("A : " + s[0]);
        ra_b.setText("B : " + s[1]);
        ra_c.setText("C : " + s[2]);

        ((RadioButton) ra_group.getChildAt(answer)).setTextColor(mContext.getResources().getColor(R.color.colorRed));
        if (answer == option_selected) {
            Toast.makeText(getApplicationContext(), "You Right!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "You Wrong!", Toast.LENGTH_SHORT).show();
        }

        setEnableRadioButton(ra_group, false);
    }

    private void showResult_part3(int position) {
//        if (position < 0 || position >= mListObj_part3.size()) {
//            return;
//        }
        int answer[];
        if (part_number == 2)
            answer = mListObj_part3.get(position).getAnswer();
        else if (part_number == 3)
            answer = mListObj_part4.get(position).getAnswer();
        else if (part_number == 5)
            answer = mListObj_part6.get(position).getAnswer();
        else
            answer = mListObj_part7.get(position).getAnswer();

        ((RadioButton) ra_group1.getChildAt(answer[0])).setTextColor(mContext.getResources().getColor(R.color.colorRed));
        ((RadioButton) ra_group2.getChildAt(answer[1])).setTextColor(mContext.getResources().getColor(R.color.colorRed));
        ((RadioButton) ra_group3.getChildAt(answer[2])).setTextColor(mContext.getResources().getColor(R.color.colorRed));

        setEnableRadioButton(ra_group1, false);
        setEnableRadioButton(ra_group2, false);
        setEnableRadioButton(ra_group3, false);

    }

    private void showResult_part4(int position) {
        int answer = mListObj_part5.get(position).getAnswer();

        ((RadioButton) ra_group.getChildAt(answer)).setTextColor(mContext.getResources().getColor(R.color.colorRed));
        if (answer == option_selected) {
            Toast.makeText(getApplicationContext(), "You Right!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "You Wrong!", Toast.LENGTH_SHORT).show();
        }

        setEnableRadioButton(ra_group, false);
    }

    private void showSentence_part12(int position) {
//        if (position < 0 || position >= mListObj_part1.size()) {
//            return;
//        }
        tv_cur.setText(1 + position + "");

        if (part_number == 0)
            tv_num.setText("/" + mListObj_part1.size());
        else
            tv_num.setText("/" + mListObj_part2.size());

        // load image
        if (part_number == 0) {
            Glide.with(mContext).load(Uri.parse(ImageUtils.loadDrawableImage(mListObj_part1.get(position_sentence_1).getUrl_photo())))
                    .centerCrop().into(imageView);
        }

        // load music
        mediaPlayer = new MediaPlayer();

        mProgressBar.setOnSeekBarChangeListener(this); // Important
        mediaPlayer.setOnCompletionListener(this);
        utils = new TimeUtils();

        tv_current_duration.setText("0:00");
        if (part_number == 0)
            tv_total_duration.setText("0:" + mListObj_part1.get(position_sentence_1).getDuration_in_seconds());
        else
            tv_total_duration.setText("0:" + mListObj_part2.get(position_sentence_2).getDuration_in_seconds());
    }


    private void showSentence_part3(int position) {
//        if (position < 0 || position >= mListObj_part3.size()) {
//            return;
//        }

        tv_cur.setText(1 + position + "");

        String[] questions = null;
        String question;
        String[] options_1 = null;
        String[] options_2 = null;
        String[] options_3 = null;

        if (part_number == 2) {
            tv_num.setText("/" + mListObj_part3.size());
            questions = mListObj_part3.get(position).getQuestions();
            options_1 = mListObj_part3.get(position).getOption_1();
            options_2 = mListObj_part3.get(position).getOption_2();
            options_3 = mListObj_part3.get(position).getOption_3();
        } else if (part_number == 3) {
            tv_num.setText("/" + mListObj_part4.size());
            questions = mListObj_part4.get(position).getQuestions();
            options_1 = mListObj_part4.get(position).getOption_1();
            options_2 = mListObj_part4.get(position).getOption_2();
            options_3 = mListObj_part4.get(position).getOption_3();
        } else if (part_number == 5) {
            tv_num.setText("/" + mListObj_part6.size());
            question = mListObj_part6.get(position).getQuestions();
            options_1 = mListObj_part6.get(position).getOption_1();
            options_2 = mListObj_part6.get(position).getOption_2();
            options_3 = mListObj_part6.get(position).getOption_3();
            String paragraph = mListObj_part6.get(position).getQuestions();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tv_paragraph.setText(Html.fromHtml(paragraph,Html.FROM_HTML_MODE_LEGACY));
            } else {
                tv_paragraph.setText(Html.fromHtml(paragraph));
            }
        } else {
            tv_num.setText("/" + mListObj_part7.size());
            questions = mListObj_part7.get(position).getQuestions();
            options_1 = mListObj_part7.get(position).getOption_1();
            options_2 = mListObj_part7.get(position).getOption_2();
            options_3 = mListObj_part7.get(position).getOption_3();
            String paragraph = mListObj_part7.get(position).getQuestion();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tv_paragraph.setText(Html.fromHtml(paragraph,Html.FROM_HTML_MODE_LEGACY));
            } else {
                tv_paragraph.setText(Html.fromHtml(paragraph));
            }
        }


        if (part_number != 5) {
            tv_question1.setText("1. " + questions[0]);
            ra_a_1.setText(options_1[0]);
            ra_b_1.setText(options_1[1]);
            ra_c_1.setText(options_1[2]);
            ra_d_1.setText(options_1[3]);

            tv_question2.setText("2. " + questions[1]);
            ra_a_2.setText(options_2[0]);
            ra_b_2.setText(options_2[1]);
            ra_c_2.setText(options_2[2]);
            ra_d_2.setText(options_2[3]);

            tv_question3.setText("3. " + questions[2]);
            ra_a_3.setText(options_3[0]);
            ra_b_3.setText(options_3[1]);
            ra_c_3.setText(options_3[2]);
            ra_d_3.setText(options_3[3]);
        } else {
            tv_question1.setText("(1) ..............");
            ra_a_1.setText(options_1[0]);
            ra_b_1.setText(options_1[1]);
            ra_c_1.setText(options_1[2]);
            ra_d_1.setText(options_1[3]);

            tv_question2.setText("(2) ..............");
            ra_a_2.setText(options_2[0]);
            ra_b_2.setText(options_2[1]);
            ra_c_2.setText(options_2[2]);
            ra_d_2.setText(options_2[3]);

            tv_question3.setText("(3) ..............");
            ra_a_3.setText(options_3[0]);
            ra_b_3.setText(options_3[1]);
            ra_c_3.setText(options_3[2]);
            ra_d_3.setText(options_3[3]);
        }

        // load music
        if (part_number == 2 || part_number == 3) {
            mediaPlayer = new MediaPlayer();
            mProgressBar.setOnSeekBarChangeListener(this); // Important
            mediaPlayer.setOnCompletionListener(this);
            utils = new TimeUtils();
            tv_current_duration.setText("0:00");
            if (part_number == 2)
                tv_total_duration.setText("0:" + mListObj_part3.get(position).getDuration_in_seconds());
            else
                tv_total_duration.setText("0:" + mListObj_part4.get(position).getDuration_in_seconds());
        }
    }

    private void downloadFile(String d0wnload_file_path, String fileName, String pathToSave) {
        int downloadedSize = 0;
        int totalSize = 0;

        try {
            URL url = new URL(d0wnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

//            File myDir;
//            myDir = new File(pathToSave);
//            myDir.mkdirs();
//
//            // create a new file, to save the downloaded file
//
//            String mFileName = fileName;
//            File file = new File(myDir, mFileName);

            String mFileName = fileName;
            FileOutputStream fileOutput = openFileOutput(mFileName, Context.MODE_PRIVATE);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            // runOnUiThread(new Runnable() {
            // public void run() {
            // pb.setMax(totalSize);
            // }
            // });

            // create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                // runOnUiThread(new Runnable() {
                // public void run() {
                // pb.setProgress(downloadedSize);
                // float per = ((float)downloadedSize/totalSize) * 100;
                // cur_val.setText("Downloaded " + downloadedSize + "KB / " +
                // totalSize + "KB (" + (int)per + "%)" );
                // }
                // });
            }
            // close the output stream when complete //
            fileOutput.close();

            Toast.makeText(mContext, "Downloaded", Toast.LENGTH_LONG).show();
            // runOnUiThread(new Runnable() {
            // public void run() {
            // // pb.dismiss(); // if you want close it..
            // }
            // });

        } catch (final MalformedURLException e) {
            // showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            // showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            // showError("Error : Please check your internet connection " + e);
        }
    }

    private void showSentence_part5(int position) {
        if (position < 0 || position >= mListObj_part5.size()) {
            return;
        }
        tv_cur.setText(1 + position + "");

        tv_num.setText("/" + mListObj_part5.size());

        String sentence = mListObj_part5.get(position).getQuestion();
        String[] options = mListObj_part5.get(position).getOptions();

        tv_part2_description.setText(sentence);
        ra_a.setText("A : " + options[0]);
        ra_b.setText("B : " + options[1]);
        ra_c.setText("C : " + options[2]);
        ra_d.setText("D : " + options[3]);
    }

    private void resetSentence_part12() {

        ra_a.setText("A");
        ra_b.setText("B");
        ra_c.setText("C");
        ra_d.setText("D");

        resetOptionsColor(ra_group);
        setEnableRadioButton(ra_group, true);

    }

    private void resetSentence_part3() {
        resetOptionsColor(ra_group1);
        resetOptionsColor(ra_group2);
        resetOptionsColor(ra_group3);
        setEnableRadioButton(ra_group1, true);
        setEnableRadioButton(ra_group2, true);
        setEnableRadioButton(ra_group3, true);
    }

    private void resetSentence_part5() {

        resetOptionsColor(ra_group);
        setEnableRadioButton(ra_group, true);
    }

    private void setOptionsColor(int position, RadioGroup radioGroup) {
        for (int i = 0; i <= 3; i++) {
            if (i == position)
                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            else
                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }
    }

    private void resetOptionsColor(RadioGroup radioGroup) {
        radioGroup.clearCheck();
        for (int i = 0; i <= 3; i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }
    }

    private void setEnableRadioButton(RadioGroup radioGroup, boolean status) {
        for (int i = 0; i <= 3; i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setEnabled(status);
        }
    }

    private void playSong(int position) {
        // Play song
        try {
            AssetFileDescriptor afd;
            if (part_number == 0 || part_number == 1)
                afd = this.getAssets().openFd(PATH_FILE_MP3 + mListObj_part1.get(position).getUrl_audio() + DUOI_FILE);
            else
                afd = this.getAssets().openFd(PATH_FILE_MP3_1 + mListObj_part3.get(position).getUrl_audio() + DUOI_FILE);

            mediaPlayer.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getLength()
            );
            afd.close();

            mediaPlayer.prepare();
//            mediaPlayer.start();

            // set Progress bar values
            mProgressBar.setProgress(0);
            mProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            tv_current_duration.setText("" + utils.milliSecondsToTimer(currentDuration));
            // Displaying time completed playing
            tv_total_duration.setText("" + utils.milliSecondsToTimer(totalDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            mProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play.setImageResource(R.drawable.action_play);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mIsCheck = false;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
        }
    }

    class SharedPreference {

        public static final String PREFS_NAME = "OUR_PREFS";
        public static final String PREFS_KEY_1 = "POSITION_SENTENCE_1";
        public static final String PREFS_KEY_2 = "POSITION_SENTENCE_2";
        public static final String PREFS_KEY_3 = "POSITION_SENTENCE_3";
        public static final String PREFS_KEY_4 = "POSITION_SENTENCE_4";
        public static final String PREFS_KEY_5 = "POSITION_SENTENCE_5";
        public static final String PREFS_KEY_6 = "POSITION_SENTENCE_6";
        public static final String PREFS_KEY_7 = "POSITION_SENTENCE_7";


        SharedPreferences settings;
        SharedPreferences.Editor editor;

        public SharedPreference() {
            super();
        }

        public void save(Context context, int pos) {
            //settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();

            if (part_number == 0)
                editor.putInt(PREFS_KEY_1, pos);
            else if (part_number == 1)
                editor.putInt(PREFS_KEY_2, pos);
            else if (part_number == 2)
                editor.putInt(PREFS_KEY_3, pos);
            else if (part_number == 3)
                editor.putInt(PREFS_KEY_4, pos);
            else if (part_number == 4)
                editor.putInt(PREFS_KEY_5, pos);
            else if (part_number == 5)
                editor.putInt(PREFS_KEY_6, pos);
            else if (part_number == 6)
                editor.putInt(PREFS_KEY_7, pos);

            editor.commit();
        }

        public int getValue(Context context) {
            int pos;
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (part_number == 0)
                pos = settings.getInt(PREFS_KEY_1, 0);
            else if (part_number == 1)
                pos = settings.getInt(PREFS_KEY_2, 0);
            else if (part_number == 2)
                pos = settings.getInt(PREFS_KEY_3, 0);
            else if (part_number == 3)
                pos = settings.getInt(PREFS_KEY_4, 0);
            else if (part_number == 4)
                pos = settings.getInt(PREFS_KEY_5, 0);
            else if (part_number == 5)
                pos = settings.getInt(PREFS_KEY_6, 0);
            else if (part_number == 6)
                pos = settings.getInt(PREFS_KEY_7, 0);
            else
                pos = 0;
            return pos;
        }

        public void clearSharedPreference(Context context) {
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();

            editor.clear();
            editor.commit();
        }
    }
}
