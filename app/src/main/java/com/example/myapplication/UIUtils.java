package com.example.myapplication;

import android.content.Context;
import android.util.DisplayMetrics;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class UIUtils {
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}