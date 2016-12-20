package info.androidhive.navigationdrawer.utils;

/**
 * Created by Laptop TCC on 12/20/2016.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();
    private static final String THREAD_NAME = "IconLoaderThread";
    private HandlerThread mHandlerThread;
    private UiUpdateHandler mUiUpdateHandler;
    private IconLoaderHandler mIconLoaderHandler;

    private Context mAppContext;
    LruCache<String, Drawable> mCache;
    String PATH = "image/";

    private static class IconInfo {

        String nameImage;
        ImageView imageView;
    }

    private class UiUpdateHandler extends Handler {

        public UiUpdateHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            IconInfo iconInfo = (IconInfo) msg.obj;
            if (iconInfo != null && (iconInfo.nameImage.equals(iconInfo.imageView.getTag()))) {
                Drawable iconDrawable = mCache.get(iconInfo.nameImage);
                if (iconDrawable != null) {
                    iconInfo.imageView.setImageDrawable(iconDrawable);
                }
            }
        }
    }

    private class IconLoaderHandler extends Handler {

        public IconLoaderHandler(Looper l) {
            super(l);
        }

        @Override
        public void handleMessage(Message msg) {
            IconInfo iconInfo = (IconInfo) msg.obj;
            if (iconInfo != null) {
                try {
                    Drawable d = loadDrawableLocal(iconInfo.nameImage);
                    if (d != null) {
                        mCache.put(iconInfo.nameImage, d);
                        mUiUpdateHandler.sendMessageAtFrontOfQueue(mUiUpdateHandler.obtainMessage(0, iconInfo));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ImageLoader(Context appContext, final int CACHE_SIZE) {
        mAppContext = appContext;
        mCache = new LruCache<>(CACHE_SIZE);
    }

    public void startIconLoaderThread() {
        mHandlerThread = new HandlerThread(THREAD_NAME);
        mHandlerThread.start();
        Looper looper = mHandlerThread.getLooper();
        if (looper != null) {
            mIconLoaderHandler = new IconLoaderHandler(looper);
        }
        mUiUpdateHandler = new UiUpdateHandler();
    }

    @SuppressLint("NewApi")
    public void stopIconLoaderThread() {
        if (mHandlerThread != null) {
            mHandlerThread.quitSafely();
        }
    }

    public void loadIcon(String pkgName, ImageView imageView) {
        if (TextUtils.isEmpty(pkgName) || imageView == null) {
            return;
        }
        imageView.setTag(pkgName);
        Drawable iconDrawable = mCache.get(pkgName);
        if (iconDrawable != null) {
            imageView.setImageDrawable(iconDrawable);
        } else {
            Log.i("duy.pq", "Load lan dau");
            IconInfo iconInfo = new IconInfo();
            iconInfo.nameImage = pkgName;
            iconInfo.imageView = imageView;
            mIconLoaderHandler
                    .sendMessageAtFrontOfQueue(mIconLoaderHandler.obtainMessage(0, iconInfo));
        }
    }

    public Drawable loadDrawableLocal(String name) {
        Drawable drawable = null;

        InputStream bitmap = null;
        try {
            bitmap = mAppContext.getAssets().open(PATH + name);
            Bitmap bit = BitmapFactory.decodeStream(bitmap);
            drawable = new BitmapDrawable(mAppContext.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return drawable;
    }

    public Drawable loadIcon(String name) {
        Drawable drawable = null;
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        drawable = mCache.get(name);
        if (drawable == null) {
            return loadDrawableLocal(name);
        }
        return drawable;
    }

}
