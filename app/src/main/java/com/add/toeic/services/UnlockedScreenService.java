package com.add.toeic.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.add.toeic.recievers.UnlockedScreenReceiver;

/**
 * Created by DTA on 12/30/2016.
 */

public class UnlockedScreenService extends Service {

    private static BroadcastReceiver mUnlockedScreenReceiver = null;

    // check whether current service alive or not to avoid re-create
    private static UnlockedScreenService mUnlockedScreenService = null;

    public static boolean isCreated() {
        return mUnlockedScreenService != null;
    }

    private static final String PREF_NAME = "saveStateToggleBtn";
    private static final String quickAnswerState = "tgbtn_unlocked_quick_answer_state";
    private SharedPreferences sharedPrefs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("anhdt", "UnlockedScreenService onCreated");

        super.onCreate();
        mUnlockedScreenService = this;
        sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (sharedPrefs != null) {
            sharedPrefs.registerOnSharedPreferenceChangeListener(mPrefsListener);
            if (mUnlockedScreenReceiver == null && sharedPrefs.getBoolean(quickAnswerState, false)) {
                mUnlockedScreenReceiver = new UnlockedScreenReceiver();
                registerReceiver(mUnlockedScreenReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(mPrefsListener);
        super.onDestroy();
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (sharedPreferences.equals(sharedPrefs)) {
                boolean newState = sharedPrefs.getBoolean(key, false);
                if (key.equalsIgnoreCase(quickAnswerState)) {
                    onUnlockedScreen(newState);
                }
            }
        }
    };

    void onUnlockedScreen(boolean isEnabled) {
        if (isEnabled) {
            if (mUnlockedScreenReceiver == null) {
                mUnlockedScreenReceiver = new UnlockedScreenReceiver();
                registerReceiver(mUnlockedScreenReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
            }
        } else {
            unregisterReceiver(mUnlockedScreenReceiver);
            mUnlockedScreenReceiver = null;
        }
    }
}
