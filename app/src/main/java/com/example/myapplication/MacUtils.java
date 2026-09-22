package com.example.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;
import java.util.Objects;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class MacUtils {
    public static void showCardKeyValidationScreen(final Activity activity) {
        String deviceId = DeviceInfoManager.getDeviceId(activity);
        if (!NetworkManager.isNetworkAvailable(activity)) {
            Toast.makeText(activity, "请打开网络连接", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            Objects.requireNonNull(activity);
            handler.postDelayed(new Runnable() {
                @Override // java.lang.Runnable
                public final void run() {
                    activity.finish();
                }
            }, 2000L);
            return;
        }
        String savedKami = CardKeyManager.getSavedKami(activity);
        if (!savedKami.isEmpty()) {
            CardKeyManager.validateKami(activity, savedKami, deviceId, true);
        } else {
            DialogUtils.showValidationDialog(activity);
        }
    }
}
