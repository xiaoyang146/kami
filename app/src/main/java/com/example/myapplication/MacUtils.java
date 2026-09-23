package com.example.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;
import java.util.Objects;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class MacUtils {
    public static void showCardKeyValidationScreen(final Activity activity) {
        final String deviceId = DeviceInfoManager.getDeviceId(activity);
        if (!NetworkManager.isNetworkAvailable(activity)) {
            Toast.makeText(activity, "请打开网络连接", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            Objects.requireNonNull(activity);
            handler.postDelayed(new Runnable() { // from class: com.example.myapplication.MacUtils$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    activity.finish();
                }
            }, 2000L);
            return;
        }
        // 网络已连接：先检查「插件更新」——开启则弹出更新对话框，
        // 未开启 / 请求失败则静默跳过，继续原有的卡密验证流程
        UpdateManager.checkPluginUpdate(activity, new Runnable() { // from class: com.example.myapplication.MacUtils$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MacUtils.continueCardKeyValidation(activity, deviceId);
            }
        });
    }

    /* 原有卡密验证流程：本地已存卡密则静默自动登录，否则弹出卡密输入框 */
    public static void continueCardKeyValidation(Activity activity, String deviceId) {
        String savedKami = CardKeyManager.getSavedKami(activity);
        if (!savedKami.isEmpty()) {
            CardKeyManager.validateKami(activity, savedKami, deviceId, true);
        } else {
            DialogUtils.showValidationDialog(activity);
        }
    }
}