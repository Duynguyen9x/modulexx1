package com.add.toeic.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.add.toeic.R;

public class PracticeToiecActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_toiec);
        mContext = this;

        initActionBar();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_front);

        View view = getSupportActionBar().getCustomView();

        String title = getIntent().getExtras().getString("keyTitle");
        getSupportActionBar().setTitle(title);

        ImageButton btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tv_cur = (TextView) view.findViewById(R.id.tv_num_cur);
        tv_cur.setText("100");

        TextView tv_num = (TextView) view.findViewById(R.id.tv_num_total);
        tv_num.setText("/200");

        TextView tv_title = (TextView) view.findViewById(R.id.tv_num_total);
        tv_num.setText("/200");

        ImageButton btn_next = (ImageButton) view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Next image", Toast.LENGTH_LONG).show();
            }
        });
    }

}
