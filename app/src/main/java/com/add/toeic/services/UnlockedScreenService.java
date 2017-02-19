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

    private static final String PREF_NAME_LOCK_SCREEN = "saveStateLockScreen";
    private static final String LOCKSCREEN_IS_OPEN = "lockscreen_is_open";
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
        sharedPrefs = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE);
        if (sharedPrefs != null) {
            sharedPrefs.registerOnSharedPreferenceChangeListener(mPrefsListener);
            if (mUnlockedScreenReceiver == null) {
                mUnlockedScreenReceiver = new UnlockedScreenReceiver();
            }
            boolean isEnabled = sharedPrefs.getBoolean(LOCKSCREEN_IS_OPEN, false);
            if (isEnabled) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_USER_PRESENT);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                registerReceiver(mUnlockedScreenReceiver, filter);
                Log.d("anhdt", "registerReceiver - 0 -");
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(mPrefsListener);
        super.onDestroy();
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (sharedPreferences.equals(sharedPrefs)) {
                boolean newState = sharedPrefs.getBoolean(key, false);
                if (key.equalsIgnoreCase(LOCKSCREEN_IS_OPEN)) {
                    onUnlockedScreen(newState);
                }
            }
        }
    };

    void onUnlockedScreen(boolean isEnabled) {
        if (isEnabled) {
            if (mUnlockedScreenReceiver == null) {
                mUnlockedScreenReceiver = new UnlockedScreenReceiver();
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_USER_PRESENT);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mUnlockedScreenReceiver, filter);
            Log.d("anhdt", "registerReceiver - 1 -");
        } else {
            unregisterReceiver(mUnlockedScreenReceiver);
            mUnlockedScreenReceiver = null;
            Log.d("anhdt", "unRegisterReceiver - 1 -");
        }
    }
}
