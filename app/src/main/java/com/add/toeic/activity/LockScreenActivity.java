package com.add.toeic.activity;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.model.Word;
import com.add.toeic.provider.AppProvider;
import com.add.toeic.services.FloatingViewService;
import com.add.toeic.utils.SoundUtis;
import com.add.toeic.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private WindowManager mWindowManager;
    private ScrollView mRelativeLayoutUnlocked;
    private LinearLayout ln_word, ln0_unlocked_choice_remember_word, ln1_unlocked_answer1,
            ln2_unlocked_answer2, ln3_unlocked_answer3, ln4_unlocked_answer4;
    private TextView tv_unlocked_word, tv_unlocked_word_type,
            tv_unlocked_answer_1,
            tv_unlocked_answer_2,
            tv_unlocked_answer_3,
            tv_unlocked_answer_4;
    private CheckBox cb_unlocked_remember_word;
    private ImageButton img_btn_lock_speaker;
    private Button btn_turn_off_lockscreen;

    private int correctWordId, inCorrectWordId1, inCorrectWordId2, inCorrectWordId3;
    private Context mContext;
    private Word correct, incorrect1, incorrect2, incorrect3;

    private int randomWordIdAns[];
    private TextView textViewAns[];

    private static final int SIZE_ALL_WORDS = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Log.d("anhdt", "LockScreenActivity onCreate");
        initLayout();
        itemClicked();
    }

    @TargetApi(21)
    @Override
    public void onBackPressed() {
        Log.d("anhdt", "@TargetApi(21) :onBackPressed");
    }

    @Override
    public void onClick(View view) {
        boolean isCorrected;
        switch (view.getId()) {
            case R.id.ln1_unlocked_answer1:
                isCorrected = ln1_unlocked_answer1.findViewById(R.id.tv_unlocked_answer_1).equals(textViewAns[0]);
                processAnswer(isCorrected, R.id.ln1_unlocked_answer1);
                break;
            case R.id.ln2_unlocked_answer2:
                isCorrected = ln2_unlocked_answer2.findViewById(R.id.tv_unlocked_answer_2).equals(textViewAns[0]);
                processAnswer(isCorrected, R.id.ln2_unlocked_answer2);
                break;
            case R.id.ln3_unlocked_answer3:
                isCorrected = ln3_unlocked_answer3.findViewById(R.id.tv_unlocked_answer_3).equals(textViewAns[0]);
                processAnswer(isCorrected, R.id.ln3_unlocked_answer3);
                break;
            case R.id.ln4_unlocked_answer4:
                isCorrected = ln4_unlocked_answer4.findViewById(R.id.tv_unlocked_answer_4).equals(textViewAns[0]);
                processAnswer(isCorrected, R.id.ln4_unlocked_answer4);
                break;
            case R.id.img_btn_lock_speaker:
                SoundUtis.play(mContext, correct.getName());
                break;
            case R.id.cb_unlocked_remember_word:
                if (cb_unlocked_remember_word.isChecked()) {
                    Toast.makeText(mContext, "Good", Toast.LENGTH_SHORT).show();
                }
                AppProvider.toggleRemember(correct, mContext, false);
                break;
            case R.id.btn_turn_off_lockscreen:
                removeLayout();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent close = new Intent(LockScreenActivity.this, MainActivity.class);
                        close.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        close.putExtra("open_drawer", true);
                        startActivity(close);
                    }
                }, 1000);
                break;

            default:
        }

    }

    private void processAnswer(boolean isCorrected, int index_choosen) {

        if (isCorrected) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.custom_toast, null);
            Toast toast = new Toast(mContext);
            toast.setView(view);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            removeLayout();
        } else {
            Utils.animShaking(mContext, mRelativeLayoutUnlocked.findViewById(index_choosen));
            mRelativeLayoutUnlocked.findViewById(index_choosen).setBackground(getResources().getDrawable(R.drawable.lock_answer_layout_bg_incorrect, getTheme()));
        }
    }

    private void initLayout() {

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        mRelativeLayoutUnlocked = (ScrollView) layoutInflater.inflate(R.layout.activity_lock_screen, null);

        ln1_unlocked_answer1 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln1_unlocked_answer1);
        ln2_unlocked_answer2 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln2_unlocked_answer2);
        ln3_unlocked_answer3 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln3_unlocked_answer3);
        ln4_unlocked_answer4 = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln4_unlocked_answer4);

        ln_word = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln_word);
        ln0_unlocked_choice_remember_word = (LinearLayout) mRelativeLayoutUnlocked.findViewById(R.id.ln0_unlocked_choice_remember_word);
        tv_unlocked_word = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_word);
        tv_unlocked_word_type = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_word_type);
        tv_unlocked_answer_1 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_1);
        tv_unlocked_answer_2 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_2);
        tv_unlocked_answer_3 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_3);
        tv_unlocked_answer_4 = (TextView) mRelativeLayoutUnlocked.findViewById(R.id.tv_unlocked_answer_4);

        cb_unlocked_remember_word = (CheckBox) mRelativeLayoutUnlocked.findViewById(R.id.cb_unlocked_remember_word);
        img_btn_lock_speaker = (ImageButton) mRelativeLayoutUnlocked.findViewById(R.id.img_btn_lock_speaker);
        btn_turn_off_lockscreen = (Button) mRelativeLayoutUnlocked.findViewById(R.id.btn_turn_off_lockscreen);

        mWindowManager.addView(mRelativeLayoutUnlocked, params);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mRelativeLayoutUnlocked.setBackground(wallpaperDrawable);

        randomWord();
        ln_word.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.rotate));
        ln0_unlocked_choice_remember_word.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_in_22));
        ln1_unlocked_answer1.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.expand_left_10));
        ln2_unlocked_answer2.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.expand_left_12));
        ln3_unlocked_answer3.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.expand_left_14));
        ln4_unlocked_answer4.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.expand_left_16));
        btn_turn_off_lockscreen.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.expand_right_22));
    }

    private void removeLayout() {
        Utils.animSlideDown(mRelativeLayoutUnlocked, 1000);
        finish();

        String PREF_NAME_CHAT_HEADER = "saveStateChatheader";
        String CHATHEADER_IS_OPEN = "chatheader_is_open";
        SharedPreferences sharedPrefs = getSharedPreferences(PREF_NAME_CHAT_HEADER, MODE_PRIVATE);

        if (sharedPrefs.getBoolean(CHATHEADER_IS_OPEN, false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mContext.startService(new Intent(mContext, FloatingViewService.class));
                }
            }, 1000);
        }
    }

    private void itemClicked() {
        ln1_unlocked_answer1.setOnClickListener(this);
        ln2_unlocked_answer2.setOnClickListener(this);
        ln3_unlocked_answer3.setOnClickListener(this);
        ln4_unlocked_answer4.setOnClickListener(this);
        cb_unlocked_remember_word.setOnClickListener(this);
        img_btn_lock_speaker.setOnClickListener(this);
        btn_turn_off_lockscreen.setOnClickListener(this);
    }

    private void randomWord() {
        Log.d("anhdt", "random start = " + System.currentTimeMillis());
        Random r = new Random();
        do {
            correctWordId = r.nextInt(SIZE_ALL_WORDS);
        } while (isRemembered(correctWordId));
        correct = AppProvider.getWordById_tb_All(correctWordId);

        do {
            inCorrectWordId1 = r.nextInt(SIZE_ALL_WORDS);
        } while (inCorrectWordId1 == correctWordId || isRemembered(inCorrectWordId1));
        incorrect1 = AppProvider.getWordById_tb_All(inCorrectWordId1);

        do {
            inCorrectWordId2 = r.nextInt(SIZE_ALL_WORDS);
        }
        while (inCorrectWordId2 == inCorrectWordId1 || inCorrectWordId2 == correctWordId || isRemembered(inCorrectWordId2));
        incorrect2 = AppProvider.getWordById_tb_All(inCorrectWordId2);

        do {
            inCorrectWordId3 = r.nextInt(SIZE_ALL_WORDS);
        }
        while (inCorrectWordId3 == inCorrectWordId2 || inCorrectWordId3 == inCorrectWordId1 || inCorrectWordId3 == correctWordId || isRemembered(inCorrectWordId3));
        incorrect3 = AppProvider.getWordById_tb_All(inCorrectWordId3);

        randomWordIdAns = new int[]{correctWordId, inCorrectWordId1, inCorrectWordId2, inCorrectWordId3};
        textViewAns = new TextView[]{tv_unlocked_answer_1, tv_unlocked_answer_2, tv_unlocked_answer_3, tv_unlocked_answer_4};

        //randomized randomWordIdAns & textViewAns
        Collections.shuffle(Arrays.asList(randomWordIdAns));
        Collections.shuffle(Arrays.asList(textViewAns));

        tv_unlocked_word.setText(correct.getName());
        tv_unlocked_word_type.setText(correct.getSound());
        textViewAns[0].setText(AppProvider.getWordById_tb_All(randomWordIdAns[0]).getName_key());
        textViewAns[1].setText(AppProvider.getWordById_tb_All(randomWordIdAns[1]).getName_key());
        textViewAns[2].setText(AppProvider.getWordById_tb_All(randomWordIdAns[2]).getName_key());
        textViewAns[3].setText(AppProvider.getWordById_tb_All(randomWordIdAns[3]).getName_key());
        Log.d("anhdt", "random end = " + System.currentTimeMillis());
    }

    private boolean isRemembered(int idWord) {
        return AppProvider.getWordById_tb_All(idWord).getRemember() != 0;
    }

    @Override
    protected void onDestroy() {
        Log.d("anhdt", "LockScreenAc destroy");
        super.onDestroy();
        if (mRelativeLayoutUnlocked.getWindowToken() != null) {
            Log.d("anhdt", "LockScreenAc destroy  mWindowManager.removeViewImmediate(mRelativeLayoutUnlocked)");
            // avoid -> has leaked window android.widget.ScrollView ... that was originally added here
            mWindowManager.removeViewImmediate(mRelativeLayoutUnlocked);
        }
    }
}
