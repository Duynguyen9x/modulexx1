package com.add.toeic.utils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.add.toeic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DTA on 12/25/2016.
 */

public class Utils {

    private static List<String> listPermissionsNeeded = new ArrayList<>();

    // This function for the permission which has defined in Manifest ramresource
    public static boolean checkAndRequestPermissions(Activity activity, String[] permissions, int REQUEST_ID_PERMISSIONS) {
        for (String permission : permissions) {
            if (!grantedPermissions(activity.getApplicationContext(), permission))
                listPermissionsNeeded.add(permission);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_PERMISSIONS);
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean grantedPermissions(Context context, String permssions) {
        if (permssions.equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            return enabledDrawOverlays(context);
        }
        return ContextCompat.checkSelfPermission(context, permssions) == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestPermissions(Activity activity, String[] permissions, int REQUEST_ID_PERMISSIONS) {
        if (isNeedGoToSetting(permissions)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            ActivityCompat.startActivityForResult(activity, intent, REQUEST_ID_PERMISSIONS, null);
        } else {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_ID_PERMISSIONS);
        }
    }

    private static boolean isNeedGoToSetting(String[] permissions) {
        return Arrays.asList(permissions).contains(Manifest.permission.SYSTEM_ALERT_WINDOW);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean enabledDrawOverlays(Context context) {
        return Settings.canDrawOverlays(context);
    }

    public static boolean isOverM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isUnderM() {
        return !isOverM();
    }

//    Above dangerous permissions: SYSTEM_ALERT_WINDOW and WRITE_SETTINGS belong to this category.
//    They must be granted, but are not visible in system settings.
//    To request for it you don't use a standard way (int checkSelfPermission (String permission))
//    but you have to check Settings.canDrawOverlays() or Settings.System.canWrite() appropriately

    public static void animSlideDown(final View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", view.getHeight());
        anim.setDuration(1000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        anim.start();
//        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(view);
    }

    public static void animShaking(Context context, View view){
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shaking);
        view.startAnimation(shake);
    }
}
