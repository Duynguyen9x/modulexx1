package com.add.toeic.observer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by DTA on 1/7/2017.
 */

public class AppObserver_Temp extends ContentObserver {

    ContentResolver mContentResolver;
    Uri mUri;

    public AppObserver_Temp(Handler handler, Context context, Uri uri) {
        super(handler);
        mContentResolver = context.getContentResolver();
        mUri = uri;
    }

    public void start(){
        mContentResolver.registerContentObserver(mUri, false, this);
    }

    public void stop(){
        mContentResolver.unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }
}
