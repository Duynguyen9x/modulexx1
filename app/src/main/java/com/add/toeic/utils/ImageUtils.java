package com.add.toeic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;

/**
 * Created by Laptop TCC on 12/20/2016.
 */
public class ImageUtils {
    private static String PATH = "image/";
    private static String PATH_TOPIC = "imgtopic/";
    private static String DUOI = ".jpg";

    public static Drawable loadDrawableLocal(Context mAppContext, String name) {
        Drawable drawable = null;

        InputStream bitmap = null;

        name = name.replace(" ", "_");
        try {
            bitmap = mAppContext.getAssets().open(PATH + name + DUOI);
            Bitmap bit = BitmapFactory.decodeStream(bitmap);
            drawable = new BitmapDrawable(mAppContext.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return drawable;
    }

    public static Drawable loadDrawableParent(Context mAppContext, int parent) {
        Drawable drawable = null;

        InputStream bitmap = null;
        int k = 51 + parent;
        String name = k + ".jpg";

        try {
            bitmap = mAppContext.getAssets().open(PATH_TOPIC + name);
            Bitmap bit = BitmapFactory.decodeStream(bitmap);
            drawable = new BitmapDrawable(mAppContext.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return drawable;
    }

    public static Drawable loadDrawableChild(Context mAppContext, int parent, int child) {
        Drawable drawable = null;

        InputStream bitmap = null;
        int k = parent * 5 + child + 1;
        String name = k + ".jpg";

        try {
            bitmap = mAppContext.getAssets().open(PATH_TOPIC + name);
            Bitmap bit = BitmapFactory.decodeStream(bitmap);
            drawable = new BitmapDrawable(mAppContext.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return drawable;
    }

    public static String loadDrawableChild(int parent) {

        int k = 51 + parent;
        String name = k + ".jpg";

        return "file:///android_asset/imgtopic/" + name;
    }

}
