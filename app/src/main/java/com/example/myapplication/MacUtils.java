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
            handler.postDelayed(new Runnable() { // from class: com.example.myapplication.MacUtils$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    activity.finish();
                }
            }, 2000L);
            return;
        }
        String savedKami = CardKeyManager.getSavedKami(activity);
        if (!savedKami.isEmpty()) {
            // 卡密校验通过后（回调内）再检查插件更新，避免和卡密弹窗同时出现
            CardKeyManager.validateKami(activity, savedKami, deviceId, true);
            checkPluginUpdate(activity);
        } else {
            DialogUtils.showValidationDialog(activity);
        }
    }

    /**
     * 检查插件更新并弹更新对话框（实际逻辑在 {@link UpdateManager}）。
     * 目前挂在「已有卡密、自动校验通过」这条路径上；未开通插件更新、或版本不比本机新时静默跳过。
     */
    public static void checkPluginUpdate(final Activity activity) {
        UpdateManager.checkUpdate(activity, null);
    }
}
