package com.example.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.HttpUrl;
import org.json.JSONObject;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class NoticeManager {
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void fetchNotices(final Activity activity, final Runnable onComplete) {
        new Thread(new Runnable() { // from class: com.example.myapplication.NoticeManager$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NoticeManager.lambda$fetchNotices$2(activity, onComplete);
            }
        }).start();
    }

    static /* synthetic */ void lambda$fetchNotices$2(final Activity activity, final Runnable onComplete) {
        JSONObject data;
        HttpURLConnection conn = null;
        try {
            try {
                String urlString = "https://ok1666.cn/APPyingyon/gonggao/get_gonggao.php?app_id=" + AppConstants.getAppId();
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
                    if (json.optBoolean("success") && (data = json.optJSONObject("data")) != null) {
                        final Notice notice = new Notice(data.optString("title", "公告"), data.optString("content", HttpUrl.FRAGMENT_ENCODE_SET));
                        handler.post(new Runnable() { // from class: com.example.myapplication.NoticeManager$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogUtils.showNoticeDialog(activity, notice, onComplete);
                            }
                        });
                        if (conn != null) {
                            conn.disconnect();
                            return;
                        }
                        return;
                    }
                }
                handler.post(onComplete);
                if (conn == null) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(new Runnable() { // from class: com.example.myapplication.NoticeManager$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        NoticeManager.lambda$fetchNotices$1(activity, onComplete);
                    }
                });
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

    static /* synthetic */ void lambda$fetchNotices$1(Activity activity, Runnable onComplete) {
        Toast.makeText(activity, "公告加载失败", Toast.LENGTH_SHORT).show();
        onComplete.run();
    }

    public static void fetchNoticesWithAppId(final Activity activity, final String appId, final Runnable onComplete) {
        new Thread(new Runnable() { // from class: com.example.myapplication.NoticeManager$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                NoticeManager.lambda$fetchNoticesWithAppId$5(appId, activity, onComplete);
            }
        }).start();
    }

    static /* synthetic */ void lambda$fetchNoticesWithAppId$5(String appId, final Activity activity, final Runnable onComplete) {
        JSONObject data;
        HttpURLConnection conn = null;
        try {
            try {
                String urlString = "https://ok1666.cn/APPyingyon/gonggao/get_gonggao.php?app_id=" + appId;
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
                    if (json.optBoolean("success") && (data = json.optJSONObject("data")) != null) {
                        final Notice notice = new Notice(data.optString("title", "公告"), data.optString("content", HttpUrl.FRAGMENT_ENCODE_SET));
                        handler.post(new Runnable() { // from class: com.example.myapplication.NoticeManager$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogUtils.showNoticeDialog(activity, notice, onComplete);
                            }
                        });
                        if (conn != null) {
                            conn.disconnect();
                            return;
                        }
                        return;
                    }
                }
                handler.post(onComplete);
                if (conn == null) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(new Runnable() { // from class: com.example.myapplication.NoticeManager$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        NoticeManager.lambda$fetchNoticesWithAppId$4(activity, onComplete);
                    }
                });
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

    static /* synthetic */ void lambda$fetchNoticesWithAppId$4(Activity activity, Runnable onComplete) {
        Toast.makeText(activity, "公告加载失败", Toast.LENGTH_SHORT).show();
        onComplete.run();
    }

    public static class Notice {
        public final String content;
        public final String title;

        public Notice(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
