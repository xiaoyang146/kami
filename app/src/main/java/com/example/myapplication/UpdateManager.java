package com.example.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class UpdateManager {
    private static final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 拉取「插件更新」配置：开启则弹出更新对话框，未开启 / 失败则直接回调进入下一步（卡密验证）。
     * 调用时机：网络检测通过之后、卡密验证之前。
     */
    public static void checkPluginUpdate(final Activity activity, final Runnable onNext) {
        new Thread(new Runnable() { // from class: com.example.myapplication.UpdateManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UpdateManager.lambda$checkPluginUpdate$0(activity, onNext);
            }
        }).start();
    }

    static /* synthetic */ void lambda$checkPluginUpdate$0(final Activity activity, final Runnable onNext) {
        JSONObject data;
        HttpURLConnection conn = null;
        try {
            try {
                String urlString = AppConstants.GENGXIN_URL + "?app_id=" + AppConstants.getAppId();
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                int code = conn.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        } else {
                            sb.append(line);
                        }
                    }
                    reader.close();
                    JSONObject json = new JSONObject(sb.toString());
                    if (json.optBoolean("success") && (data = json.optJSONObject("data")) != null
                            && data.optInt("enabled", 0) == 1) {
                        final String updateTitle = data.optString("update_title", "发现新版本");
                        final String updateContent = data.optString("update_content", "");
                        final String updateUrl = data.optString("update_url", "");
                        final boolean forceUpdate = data.optInt("force_update", 0) == 1;
                        if (updateUrl.length() > 0) {
                            handler.post(new Runnable() { // from class: com.example.myapplication.UpdateManager$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogUtils.showUpdateDialog(activity, updateTitle, updateContent, updateUrl, forceUpdate, onNext);
                                }
                            });
                            if (conn != null) {
                                conn.disconnect();
                                return;
                            }
                            return;
                        }
                    }
                }
                handler.post(onNext);
                if (conn == null) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(onNext);
                if (0 == 0) {
                    return;
                }
            }
            conn.disconnect();
        } catch (Throwable th) {
            if (0 != 0) {
                conn.disconnect();
            }
            throw th;
        }
    }
}