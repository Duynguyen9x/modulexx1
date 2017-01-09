package com.add.toeic.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.add.toeic.R;
import com.add.toeic.activity.MainActivity;
import com.add.toeic.model.Word;
import com.add.toeic.utils.WordUtils;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class FloatingViewService extends Service implements SpringListener {

    private WindowManager mWindowManager;
    private View mFloatingView;
    private Context mContext;
    private static ArrayList<Word> arrayList;
    private CountDownTimer mCountDownTimer;
    private static int TIME_COUNT_DOWN = 1000;
    private Timer mTimer;
    public static int posi_play;
    private TextView tv_word, tv_word_sound, tv_word_key, tv_detail_word;
    private TextView tv_detail_word_key, tv_detail_word_sound, tv_detail_example, tv_detail_example_key, tv_details_num;
    private Handler mHandler = new Handler();
    private ImageView closeButtonCollapsed, playButton, nextButton, prevButton, closeButton, openButton;
    private Button btn_time;
    boolean isRun = true;
    View collapsedView, expandedView;
    WindowManager.LayoutParams params;
    private CheckBox cbShowVietName, cbRememberWord;
    boolean isShowVietnam = true;
    boolean isRemember = false;
    boolean isExplore = false;

    private static double TENSION = 800;
    private static double DAMPER = 20; //friction
    private SpringSystem mSpringSystem;
    private Spring mSpring;

    public FloatingViewService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initView();
        arrayList = getDataFull(mContext);
        initControl();
        auto();

        mSpringSystem = SpringSystem.create();

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);
    }


    private void initView() {
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        expandedView = mFloatingView.findViewById(R.id.expanded_container);


        //load textview
        tv_word = (TextView) mFloatingView.findViewById(R.id.tv_word);
        tv_word_sound = (TextView) mFloatingView.findViewById(R.id.tv_word_sound);
        tv_word_key = (TextView) mFloatingView.findViewById(R.id.tv_word_key);

        tv_detail_word = (TextView) mFloatingView.findViewById(R.id.tv_detail_word);
        tv_detail_word_key = (TextView) mFloatingView.findViewById(R.id.tv_detail_word_key);
        tv_detail_word_sound = (TextView) mFloatingView.findViewById(R.id.tv_detail_word_sound);
        tv_detail_example = (TextView) mFloatingView.findViewById(R.id.tv_detail_word_example);
        tv_detail_example_key = (TextView) mFloatingView.findViewById(R.id.tv_detail_word_example_key);
        tv_details_num = (TextView) mFloatingView.findViewById(R.id.tv_detail_num);

        //Set the close button
        closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        playButton = (ImageView) mFloatingView.findViewById(R.id.play_btn);
        nextButton = (ImageView) mFloatingView.findViewById(R.id.next_btn);
        prevButton = (ImageView) mFloatingView.findViewById(R.id.prev_btn);
        openButton = (ImageView) mFloatingView.findViewById(R.id.open_button);

        // check box
        cbShowVietName = (CheckBox) mFloatingView.findViewById(R.id.cb_detail_check);
        cbRememberWord = (CheckBox) mFloatingView.findViewById(R.id.cb_detail_check_good);

        //button
        btn_time = (Button) mFloatingView.findViewById(R.id.btn_time);

    }

    ;

    private void initControl() {
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Toast.makeText(FloatingViewService.this, "Playing the song.", Toast.LENGTH_LONG).show();
                posi_play--;

                auto();
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                isExplore = false;
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        });


        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBack();
            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FloatingViewService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //close the service and remove view from the view hierarchy
                stopSelf();
            }
        });

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        mSpring.setEndValue(1f);
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                                pauseWord();

                                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.fade);
                                expandedView.startAnimation(animation);
                                isExplore = true;
                            }
                        }
                        mSpring.setEndValue(0f);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        cbShowVietName.setChecked(true);

        cbShowVietName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_word_key.setVisibility(View.VISIBLE);
                    tv_detail_word_key.setVisibility(View.VISIBLE);
                    tv_detail_example_key.setVisibility(View.VISIBLE);
                } else {
                    tv_word_key.setVisibility(View.GONE);
                    tv_detail_word_key.setVisibility(View.GONE);
                    tv_detail_example_key.setVisibility(View.GONE);
                }
            }
        });

        cbRememberWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemember = isChecked;
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TIME_COUNT_DOWN == 3000) {
                    btn_time.setText("5");
                    TIME_COUNT_DOWN = 5000;
                } else if (TIME_COUNT_DOWN == 5000) {
                    btn_time.setText("7");
                    TIME_COUNT_DOWN = 7000;
                } else if (TIME_COUNT_DOWN == 7000) {
                    btn_time.setText("1");
                    TIME_COUNT_DOWN = 1000;
                } else if (TIME_COUNT_DOWN == 1000) {
                    btn_time.setText("3");
                    TIME_COUNT_DOWN = 3000;
                }
            }
        });
    }

    ;

    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        pauseWord();
    }

    public ArrayList<Word> getDataFull(Context context) {
        Log.i("duy.pqq", "time1=" + System.currentTimeMillis());
        try {

            ArrayList<Word> arr = WordUtils.readAllData(mContext);

//
//            ArrayList<Word> arr = new ArrayList<Word>();
//
//            Word w1 = new Word();
//            w1.setName("Go1");
//            w1.setName_key("di1");
//            w1.setSound("/ gaau /");
//           // w1.setExamle("I want to go home");
//            w1.setExample_key("Toi di hoc");
//            arr.add(w1);
//
//            Word w2 = new Word();
//            w2.setName("Go2");
//            w2.setName_key("di2");
//            w2.setSound("/ gaau /");
//           // w2.setExamle("I go to school ");
//            w2.setExample_key("Toi di hoc");
//            arr.add(w2);
//
//            Word w3 = new Word();
//            w3.setName("Go3");
//            w3.setName_key("di3");
//            w3.setSound("/ gaau /");
//           // w3.setExamle("I go to school ");
//            w3.setExample_key("Toi di hoc");
//            arr.add(w3);
//
//            Word w4 = new Word();
//            w4.setName("Go4");
//            w4.setName_key("di4");
//            w4.setSound("/ gaau /");
//           // w4.setExamle("I go to school ");
//            w4.setExample_key("Toi di hoc");
//            arr.add(w4);

            return arr;

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("duy.pqq", "time2=" + System.currentTimeMillis());
        return null;
    }

    public void playNext() {
        remember();
        if (arrayList == null) {
            return;
        }
        if (posi_play == arrayList.size() - 1) {
            posi_play = 0;
        } else {
            posi_play++;
        }
        playWord();
    }

    public void playBack() {
        remember();
        if (arrayList == null) {
            return;
        } else if (posi_play == 0) {
            posi_play = arrayList.size() - 1;
        } else {
            posi_play--;
        }
        playWord();
    }

    public void playWord() {
        remember();

        if (arrayList.size() == 0) {
            stopSelf();
            return;
        }
        setViewWord(arrayList.get(posi_play));
    }

    public void auto() {
        isRun = true;
        mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, TIME_COUNT_DOWN);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.1f);
        collapsedView.setScaleX(scale);
        collapsedView.setScaleY(scale);

        if (isExplore) {
            expandedView.setScaleX(scale);
            expandedView.setScaleY(scale);
        }
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    playNext();
                }
            });
        }

    }

    private void remember() {
        if (isRemember) {
            arrayList.remove(arrayList.get(posi_play));
            // remove this data in database
            isRemember = false;
            posi_play--;
            if (posi_play == -1) posi_play = 0;
            cbRememberWord.setChecked(false);
        }
    }


    public void pauseWord() {
        if (mTimer != null)
            mTimer.cancel();
    }

    private void setViewWord(Word w) {
        //tv_word.setText(" (" + w.getName() + " : " + w.getName_key() + ") ");

        tv_word.setText(w.getName());
        tv_word_key.setText(w.getName_key());
        tv_word_sound.setText(w.getSound());

        tv_detail_word.setText(w.getName());
        tv_detail_word_key.setText(w.getName_key());
        tv_detail_word_sound.setText(w.getSound());

        //    tv_detail_example.setText(w.getExamle());
        tv_detail_example_key.setText(w.getExample_key());
        tv_details_num.setText("(" + (posi_play + 1) + ":" + arrayList.size() + ")");
    }

}
