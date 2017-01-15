package com.add.toeic.activity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
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
import com.add.toeic.utils.SoundUtis;
import com.add.toeic.utils.Utils;

import java.util.ArrayList;
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

    private ArrayList<Word> arrWord;
    private int correctWordId, inCorrectWordId1, inCorrectWordId2, inCorrectWordId3;
    private Context mContext;

    int randomWordIdAns[];
    TextView textViewAns[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Log.d("anhdt", "LockScreenActivity onCreate");
        arrWord = AppProvider.getAllWords(false);

        initLayout();
        itemClicked();
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
                SoundUtis.play(mContext, arrWord.get(correctWordId).getName());
                break;
            case R.id.cb_unlocked_remember_word:
                if (cb_unlocked_remember_word.isChecked())
                    Toast.makeText(mContext, "Good", Toast.LENGTH_SHORT).show();
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
        ln0_unlocked_choice_remember_word.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_in));
        ln1_unlocked_answer1.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.scale_full_width10));
        ln2_unlocked_answer2.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.scale_full_width12));
        ln3_unlocked_answer3.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.scale_full_width14));
        ln4_unlocked_answer4.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.scale_full_width16));

    }

    private void removeLayout() {
        Utils.animSlideDown(mRelativeLayoutUnlocked);
        finish();
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
        Random r = new Random();
        correctWordId = r.nextInt(arrWord.size());
        do {
            inCorrectWordId1 = r.nextInt(arrWord.size());
        } while (inCorrectWordId1 == correctWordId);

        do {
            inCorrectWordId2 = r.nextInt(arrWord.size());
        } while (inCorrectWordId2 == inCorrectWordId1 || inCorrectWordId2 == correctWordId);

        do {
            inCorrectWordId3 = r.nextInt(arrWord.size());
        }
        while (inCorrectWordId3 == inCorrectWordId2 || inCorrectWordId3 == inCorrectWordId1 || inCorrectWordId3 == correctWordId);

        randomWordIdAns = new int[]{correctWordId, inCorrectWordId1, inCorrectWordId2, inCorrectWordId3};
        textViewAns = new TextView[]{tv_unlocked_answer_1, tv_unlocked_answer_2, tv_unlocked_answer_3, tv_unlocked_answer_4};

        //randomized randomWordIdAns & textViewAns
        Collections.shuffle(Arrays.asList(randomWordIdAns));
        Collections.shuffle(Arrays.asList(textViewAns));

        tv_unlocked_word.setText(arrWord.get(correctWordId).getName());
        tv_unlocked_word_type.setText(arrWord.get(correctWordId).getSound());
        textViewAns[0].setText(arrWord.get(randomWordIdAns[0]).getName_key());
        textViewAns[1].setText(arrWord.get(randomWordIdAns[1]).getName_key());
        textViewAns[2].setText(arrWord.get(randomWordIdAns[2]).getName_key());
        textViewAns[3].setText(arrWord.get(randomWordIdAns[3]).getName_key());
    }
}
