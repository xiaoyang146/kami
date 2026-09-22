package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import java.util.UUID;
import okhttp3.HttpUrl;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class DeviceInfoManager {
    public static String getDeviceId(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (androidId == null || androidId.isEmpty() || "9774d56d682e549c".equals(androidId)) {
                String deviceInfo = "android-" + Build.BOARD + "/" + Build.BRAND + "/" + Build.DEVICE + "/" + Build.MODEL + "/" + Build.PRODUCT + "/" + (Build.SERIAL != null ? Build.SERIAL : "unknown");
                return UUID.nameUUIDFromBytes(deviceInfo.getBytes()).toString().replace("-", HttpUrl.FRAGMENT_ENCODE_SET);
            }
            return androidId;
        } catch (Exception e) {
            e.printStackTrace();
            return UUID.randomUUID().toString().replace("-", HttpUrl.FRAGMENT_ENCODE_SET);
        }
    }
}