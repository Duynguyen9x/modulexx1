package com.add.toeic.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.add.toeic.activity.LockScreenActivity;
import com.add.toeic.services.FloatingViewService;

/**
 * Created by DTA on 12/30/2016.
 */

public class UnlockedScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("anhdt", "action = " + intent.getAction());
        if (Intent.ACTION_USER_PRESENT.equalsIgnoreCase(intent.getAction())) {
            Intent overlayIntent = new Intent(context.getApplicationContext(), LockScreenActivity.class);
            overlayIntent.addFlags(/*Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | */Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(overlayIntent);
        }
        if (Intent.ACTION_SCREEN_OFF.equalsIgnoreCase(intent.getAction())) {
            Log.d("anhdt", "stopService FloatingViewService");
            context.stopService(new Intent(context, FloatingViewService.class));
        }
    }
}
