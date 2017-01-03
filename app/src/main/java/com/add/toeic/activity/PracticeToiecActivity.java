package com.add.toeic.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.model.practice.ListenShort;
import com.add.toeic.utils.json.JsonListenShortUtils;

import java.io.IOException;
import java.util.ArrayList;

public class PracticeToiecActivity extends AppCompatActivity {

    private Context mContext;
    private ImageButton btn_play;
    private SeekBar progressBar;
    ArrayList<ListenShort> arr;
    ImageButton btn_back, btn_next, btn_previous;
    Button btn_check;
    TextView tv_cur, tv_num;
    int postion_sentence;
    RadioButton ra_a, ra_b, ra_c, ra_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_toiec);
        mContext = this;
        postion_sentence = 0;  // co the luu trong share Referrence


        initActionBar();
        initView();
        initControl();

        showSentence(postion_sentence);
        resetSentence();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    private void initControl() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play music
            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult(postion_sentence);
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSentence();
                postion_sentence--;
                if (postion_sentence == -1) {
                    postion_sentence = arr.size() - 1;
                }

                showSentence(postion_sentence);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postion_sentence++;
                if (postion_sentence == arr.size()) {
                    postion_sentence = 0;
                }
                resetSentence();
                showSentence(postion_sentence);

            }
        });

    }

    private void initView() {
        ra_a = (RadioButton) findViewById(R.id.rad_a);
        ra_b = (RadioButton) findViewById(R.id.rad_b);
        ra_c = (RadioButton) findViewById(R.id.rad_c);
        ra_d = (RadioButton) findViewById(R.id.rad_d);


        View includedLayout = findViewById(R.id.custom_layout_below);
        btn_play = (ImageButton) includedLayout.findViewById(R.id.btn_play);
        btn_check = (Button) includedLayout.findViewById(R.id.btn_check);
        progressBar = (SeekBar) includedLayout.findViewById(R.id.progressBar);

        try {
            arr = JsonListenShortUtils.readerOjectFromJson(this, "l1_photo.json");
            Log.i("duy.pq", "item=" + arr.get(0).toString());

            for (int i = 0; i < arr.size(); i++) {
                Log.i("duy.pq", "PracticeToiecActivity.item=" + arr.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_front);

        View view = getSupportActionBar().getCustomView();

        String title = getIntent().getExtras().getString("keyTitle");
        getSupportActionBar().setTitle(title);

        btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        btn_next = (ImageButton) view.findViewById(R.id.btn_next);
        btn_previous = (ImageButton) view.findViewById(R.id.btn_previous);


        tv_cur = (TextView) view.findViewById(R.id.tv_num_cur);
        tv_cur.setText("100");

        tv_num = (TextView) view.findViewById(R.id.tv_num_total);
        tv_num.setText("/200");

//        TextView tv_title = (TextView) view.findViewById(R.id.tv_num_total);
//        tv_num.setText("/200");


    }

    private void showResult(int position) {
        if (position < 0 || position >= arr.size()) {
            return;
        }
        String[] s = arr.get(postion_sentence).getOptions();
        ra_a.setText("A : " + s[0]);
        ra_b.setText("B : " + s[1]);
        ra_c.setText("C : " + s[2]);
        ra_d.setText("D : " + s[3]);
    }

    private void showSentence(int position) {
        if (position < 0 || position >= arr.size()) {
            return;
        }
        tv_cur.setText(1 + position + "");

        tv_num.setText("/" + arr.size());

        // load image
        // load music


    }

    private void resetSentence() {

        ra_a.setText("A");
        ra_b.setText("B");
        ra_c.setText("C");
        ra_d.setText("D");

    }

}
