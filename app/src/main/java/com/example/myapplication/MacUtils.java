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
            // 已有卡密：更新检查在「卡密校验成功」回调里触发（见 CardKeyManager 成功分支），避免与卡密弹窗叠框
            CardKeyManager.validateKami(activity, savedKami, deviceId, true);
        } else {
            // 没有卡密：先弹卡密输入框，同时（略延迟）触发更新检查，保证「每次打开卡密弹窗对话框」都能看到更新提示
            DialogUtils.showValidationDialog(activity);
            checkPluginUpdateDelayed(activity);
        }
    }

    /**
     * 检查插件更新并弹更新对话框（实际逻辑在 {@link UpdateManager}）。
     * 管理端「开启更新插件」为真时每次都会弹；未开启则静默跳过。
     */
    public static void checkPluginUpdate(final Activity activity) {
        UpdateManager.checkUpdate(activity, null);
    }

    /** 延迟一小段时间再检查更新，避免和刚弹出的卡密输入框互相遮挡 */
    public static void checkPluginUpdateDelayed(final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPluginUpdate(activity);
            }
        }, 600L);
    }
}
