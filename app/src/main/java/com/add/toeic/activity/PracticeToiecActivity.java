package com.add.toeic.activity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.model.practice.ListenLong;
import com.add.toeic.model.practice.ListenShort;
import com.add.toeic.utils.ImageUtils;
import com.add.toeic.utils.TimeUtils;
import com.add.toeic.utils.json.JsonListenLongUtils;
import com.add.toeic.utils.json.JsonListenShortUtils;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class PracticeToiecActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private Context mContext;
    private ImageView imageView;
    private SeekBar mProgressBar;
    ArrayList<ListenShort> listObj_part1;
    ImageButton btn_play, btn_back, btn_next, btn_previous;
    Button btn_check;
    TextView tv_title, tv_cur, tv_num, tv_current_duration, tv_total_duration, tv_part2_description;
    int position_sentence, option_selected;
    RadioGroup ra_group;
    RadioButton ra_a, ra_b, ra_c, ra_d;

    MediaPlayer mediaPlayer;
    LayoutInflater inflater;

    // object for part 3
    RadioGroup ra_group1, ra_group2, ra_group3;
    RadioButton ra_a_1, ra_b_1, ra_c_1, ra_d_1, ra_a_2, ra_b_2, ra_c_2, ra_d_2, ra_a_3, ra_b_3, ra_c_3, ra_d_3;
    ArrayList<ListenLong> listObj_part3;
    int option_selected_1, option_selected_2, option_selected_3;
    TextView tv_question1, tv_question2, tv_question3;

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    TimeUtils utils;
    int part_number;

    private static final String PATH_FILE_MP3 = "mp3practice/";
    private static final String PATH_FILE_MP3_1 = "mp3practice1/";
    private static final String DUOI_FILE = ".mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_toiec);
        mContext = this;
        position_sentence = 0;  // co the luu trong share Referrence
        option_selected = 0;

        part_number = getIntent().getIntExtra("partNum", 0);

        switch (part_number) {
            // view for part 1 & 2
            case 0:
            case 1:
                initActionBar();
                initView_part1();
                initControl_part12();
                showSentence_part12(position_sentence);
                resetSentence_part12();
                break;
            case 2:
                initActionBar();
                initView_part3();
                initControl_part3();
                showSentence_part3(position_sentence);
                resetSentence_part3();
                break;
            case 3:
                break;
            case 4:
                break;
        }
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
                        ra_a.setTextColor(getResources().getColor(R.color.colorBlue2));
                        ra_b.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_c.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_d.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                    case R.id.rad_b:
                        option_selected = 1;
                        ra_a.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_b.setTextColor(getResources().getColor(R.color.colorBlue2));
                        ra_c.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_d.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                    case R.id.rad_c:
                        option_selected = 2;
                        ra_a.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_b.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_c.setTextColor(getResources().getColor(R.color.colorBlue2));
                        ra_d.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                    case R.id.rad_d:
                        option_selected = 3;
                        ra_a.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_b.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_c.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_d.setTextColor(getResources().getColor(R.color.colorBlue2));
                        break;
                    default:
                        break;
                }
            }
        });

        imageView = (ImageView) findViewById(R.id.img_description);

        View includedLayout = findViewById(R.id.custom_layout_below);
        btn_play = (ImageButton) includedLayout.findViewById(R.id.btn_play);
        btn_check = (Button) includedLayout.findViewById(R.id.btn_check);
        mProgressBar = (SeekBar) includedLayout.findViewById(R.id.progressBar);
        tv_current_duration = (TextView) includedLayout.findViewById(R.id.tv_currentDuration);
        tv_total_duration = (TextView) includedLayout.findViewById(R.id.tv_totalDuration);


        try {
            listObj_part1 = JsonListenShortUtils.readerOjectFromJson(this, "l1_photo.json");
            Log.i("duy.pq", "item=" + listObj_part1.get(0).toString());

            for (int i = 0; i < listObj_part1.size(); i++) {
                Log.i("duy.pq", "PracticeToiecActivity.item=" + listObj_part1.get(i).toString());
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

        ra_group = (RadioGroup) findViewById(R.id.grp_answer);
        ra_a = (RadioButton) findViewById(R.id.rad_a);
        ra_b = (RadioButton) findViewById(R.id.rad_b);
        ra_c = (RadioButton) findViewById(R.id.rad_c);

        ra_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rad_a:
                        option_selected = 0;
                        ra_a.setTextColor(getResources().getColor(R.color.colorBlue2));
                        ra_b.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_c.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                    case R.id.rad_b:
                        option_selected = 1;
                        ra_a.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_b.setTextColor(getResources().getColor(R.color.colorBlue2));
                        ra_c.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                    case R.id.rad_c:
                        option_selected = 2;
                        ra_a.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_b.setTextColor(getResources().getColor(R.color.colorBlack));
                        ra_c.setTextColor(getResources().getColor(R.color.colorBlue2));
                        break;
                    default:
                        break;
                }
            }
        });

        tv_part2_description = (TextView) findViewById(R.id.tv_description);

        View includedLayout = findViewById(R.id.custom_layout_below);
        btn_play = (ImageButton) includedLayout.findViewById(R.id.btn_play);
        btn_check = (Button) includedLayout.findViewById(R.id.btn_check);
        mProgressBar = (SeekBar) includedLayout.findViewById(R.id.progressBar);
        tv_current_duration = (TextView) includedLayout.findViewById(R.id.tv_currentDuration);
        tv_total_duration = (TextView) includedLayout.findViewById(R.id.tv_totalDuration);


        try {
            listObj_part1 = JsonListenShortUtils.readerOjectFromJson(this, "l1_photo.json");
            Log.i("duy.pq", "item=" + listObj_part1.get(0).toString());

            for (int i = 0; i < listObj_part1.size(); i++) {
                Log.i("duy.pq", "PracticeToiecActivity.item=" + listObj_part1.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView_part3() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(mContext);

        FrameLayout content_practice_toeic_3 = (FrameLayout) findViewById(R.id.content_practice_toeic_part1);
        content_practice_toeic_3.removeAllViews();

        View content_practice_3 = inflater.inflate(R.layout.srcoll_view_pratice_toeic, null);
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
                        setColorOptions3(0, ra_group1);
                        break;
                    case R.id.rbtn_b_1:
                        option_selected_1 = 1;
                        setColorOptions3(1, ra_group1);
                        break;
                    case R.id.rbtn_c_1:
                        option_selected_1 = 2;
                        setColorOptions3(2, ra_group1);
                        break;
                    case R.id.rbtn_d_1:
                        option_selected_1 = 3;
                        setColorOptions3(3, ra_group1);
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
                        setColorOptions3(0, ra_group2);
                        break;
                    case R.id.rbtn_b_2:
                        option_selected_2 = 1;
                        setColorOptions3(1, ra_group2);
                        break;
                    case R.id.rbtn_c_2:
                        option_selected_2 = 2;
                        setColorOptions3(2, ra_group2);
                        break;
                    case R.id.rbtn_d_2:
                        option_selected_2 = 3;
                        setColorOptions3(3, ra_group2);
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
                        setColorOptions3(0, ra_group3);
                        break;
                    case R.id.rbtn_b_3:
                        option_selected_3 = 1;
                        setColorOptions3(1, ra_group3);
                        break;
                    case R.id.rbtn_c_3:
                        option_selected_3 = 2;
                        setColorOptions3(2, ra_group3);
                        break;
                    case R.id.rbtn_d_3:
                        option_selected_3 = 3;
                        setColorOptions3(3, ra_group3);
                        break;
                    default:
                        break;
                }
            }
        });

        View includedLayout = findViewById(R.id.custom_layout_below);
        btn_play = (ImageButton) includedLayout.findViewById(R.id.btn_play);
        btn_check = (Button) includedLayout.findViewById(R.id.btn_check);
        mProgressBar = (SeekBar) includedLayout.findViewById(R.id.progressBar);
        tv_current_duration = (TextView) includedLayout.findViewById(R.id.tv_currentDuration);
        tv_total_duration = (TextView) includedLayout.findViewById(R.id.tv_totalDuration);


        try {
            listObj_part3 = JsonListenLongUtils.readerOjectFromJson(this, "l1_short_talk.json");
            Log.i("duy.pq", "item=" + listObj_part3.get(0).toString());

            for (int i = 0; i < listObj_part3.size(); i++) {
                Log.i("duy.pq", "PracticeToiecActivity.item=" + listObj_part3.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSentence_part3(int position) {
        if (position < 0 || position >= listObj_part3.size()) {
            return;
        }
        tv_cur.setText(1 + position + "");

        tv_num.setText("/" + listObj_part3.size());

        String[] questions = listObj_part3.get(position).getQuestions();
        String[] options_1 = listObj_part3.get(position).getOption_1();
        String[] options_2 = listObj_part3.get(position).getOption_2();
        String[] options_3 = listObj_part3.get(position).getOption_3();

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
        // load music
        mediaPlayer = new MediaPlayer();

        mProgressBar.setOnSeekBarChangeListener(this); // Important
        mediaPlayer.setOnCompletionListener(this);
        utils = new TimeUtils();

        tv_current_duration.setText("0:00");
        tv_total_duration.setText("0:" + listObj_part3.get(position).getDuration_in_seconds());
    }

    private void initControl_part3() {
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
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();

                        // Changing button image to play button
                        btn_play.setImageDrawable(getResources().getDrawable(R.drawable.action_play, mContext.getTheme()));
                    }
                } else {
                    // Resume song
                    if (mediaPlayer != null) {
                        playSong(position_sentence);
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
                showResult_part3(position_sentence);
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_sentence--;
                if (position_sentence == -1) {
                    position_sentence = listObj_part3.size() - 1;
                }

                mediaPlayer.pause();
                mediaPlayer.stop();
                resetSentence_part3();
                showSentence_part3(position_sentence);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_sentence++;

                if (position_sentence == listObj_part3.size()) {
                    position_sentence = 0;
                }

                mediaPlayer.pause();
                mediaPlayer.stop();
                resetSentence_part3();
                showSentence_part3(position_sentence);
            }
        });
    }

    private void setColorOptions3(int position, RadioGroup radioGroup) {
        for (int i = 0; i <= 3; i++) {
            if (i == position)
                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(mContext.getResources().getColor(R.color.colorBlue2));
            else
                ((RadioButton) radioGroup.getChildAt(i)).setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }
    }

    private void resetColorOption3(RadioGroup radioGroup) {
        for (int i = 0; i <= 3; i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setChecked(false);
            ((RadioButton) radioGroup.getChildAt(i)).setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }
    }

    private void resetSentence_part3() {
        resetColorOption3(ra_group1);
        resetColorOption3(ra_group2);
        resetColorOption3(ra_group3);
    }

    private void initControl_part12() {

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
                        playSong(position_sentence);
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
                showResult_part1(position_sentence);
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSentence_part12();
                position_sentence--;
                if (position_sentence == -1) {
                    if (part_number == 0)
                        position_sentence = listObj_part1.size() - 1;
                    else
                        position_sentence = listObj_part1.size() - 1;
                }

                mediaPlayer.pause();
                mediaPlayer.stop();
                showSentence_part12(position_sentence);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_sentence++;
                if (part_number == 0) {
                    if (position_sentence == listObj_part1.size()) {
                        position_sentence = 0;
                    }
                } else {
                    if (position_sentence == listObj_part1.size()) {
                        position_sentence = 0;
                    }
                }
                resetSentence_part12();
                mediaPlayer.pause();
                mediaPlayer.stop();
                showSentence_part12(position_sentence);
            }
        });

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

    private void showResult_part1(int position) {
        if (position < 0 || position >= listObj_part1.size()) {
            return;
        }
        String[] s = listObj_part1.get(position_sentence).getOptions();
        int answer = listObj_part1.get(position_sentence).getAnswer();
        ra_a.setText("A : " + s[0]);
        ra_b.setText("B : " + s[1]);
        ra_c.setText("C : " + s[2]);
        ra_d.setText("D : " + s[3]);

        if (answer == option_selected) {
            ((RadioButton) ra_group.getChildAt(answer)).setTextColor(mContext.getResources().getColor(R.color.colorRed));
            Toast.makeText(getApplicationContext(), "You Right!", Toast.LENGTH_SHORT).show();
        } else {
            ((RadioButton) ra_group.getChildAt(answer)).setTextColor(mContext.getResources().getColor(R.color.colorRed));
            ((RadioButton) ra_group.getChildAt(option_selected)).setTextColor(mContext.getResources().getColor(R.color.colorBlue2));
            Toast.makeText(getApplicationContext(), "You Wrong!", Toast.LENGTH_SHORT).show();
        }

        ra_a.setEnabled(false);
        ra_b.setEnabled(false);
        ra_c.setEnabled(false);
        ra_d.setEnabled(false);
    }

    private void showResult_part3(int position) {
        if (position < 0 || position >= listObj_part3.size()) {
            return;
        }
        int answer[] = listObj_part3.get(position).getAnswer();

        ((RadioButton) ra_group1.getChildAt(answer[0])).setTextColor(mContext.getResources().getColor(R.color.colorRed));
        ((RadioButton) ra_group2.getChildAt(answer[1])).setTextColor(mContext.getResources().getColor(R.color.colorRed));
        ((RadioButton) ra_group3.getChildAt(answer[2])).setTextColor(mContext.getResources().getColor(R.color.colorRed));

    }

    private void showSentence_part12(int position) {
        if (position < 0 || position >= listObj_part1.size()) {
            return;
        }
        tv_cur.setText(1 + position + "");

        tv_num.setText("/" + listObj_part1.size());

        // load image
//        if (part_number == 0)
            Glide.with(mContext).load(Uri.parse(ImageUtils.loadDrawableImage(listObj_part1.get(position_sentence).getUrl_photo())))
                .centerCrop().into(imageView);

        // load music
        mediaPlayer = new MediaPlayer();

        mProgressBar.setOnSeekBarChangeListener(this); // Important
        mediaPlayer.setOnCompletionListener(this);
        utils = new TimeUtils();

        tv_current_duration.setText("0:00");
        tv_total_duration.setText("0:" + listObj_part1.get(position_sentence).getDuration_in_seconds());
    }

    private void playSong(int position) {
        // Play song
        try {
            AssetFileDescriptor afd;
            if (part_number == 0 || part_number == 1)
                afd = this.getAssets().openFd(PATH_FILE_MP3 + listObj_part1.get(position).getUrl_audio() + DUOI_FILE);
            else
                afd = this.getAssets().openFd(PATH_FILE_MP3_1 + listObj_part3.get(position).getUrl_audio() + DUOI_FILE);
            mediaPlayer.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getLength()
            );
            afd.close();
            mediaPlayer.prepare();
            // mediaPlayer.start();
            // Displaying Song title

            // Changing Button Image to pause image
            //btn_play.setImageResource(R.drawable.action_pause);

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
//            tv_total_duration.setText("" + utils.milliSecondsToTimer(totalDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            mProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    private void resetSentence_part12() {

        ra_a.setText("A");
        ra_a.setChecked(false);
        ra_b.setText("B");
        ra_b.setChecked(false);
        ra_c.setText("C");
        ra_c.setChecked(false);
        ra_d.setText("D");
        ra_d.setChecked(false);

        ra_a.setTextColor(getResources().getColor(R.color.colorBlack));
        ra_b.setTextColor(getResources().getColor(R.color.colorBlack));
        ra_c.setTextColor(getResources().getColor(R.color.colorBlack));
        ra_d.setTextColor(getResources().getColor(R.color.colorBlack));

        ra_a.setEnabled(true);
        ra_b.setEnabled(true);
        ra_c.setEnabled(true);
        ra_d.setEnabled(true);
    }

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

}
