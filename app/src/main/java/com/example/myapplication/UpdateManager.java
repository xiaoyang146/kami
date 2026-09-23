package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 插件更新管理器。
 *
 * 入口：{@link #checkUpdate(Activity, Runnable)}，需要弹更新对话框就弹，弹完再执行 onComplete。
 * 写法与 NoticeManager / MacUtils 保持一致：静态入口 + 后台线程请求 + 主线程回调。
 *
 * 服务端接口（GET，返回 JSON）：
 * <pre>
 *   {PLUGIN_UPDATE_URL}?app_id={AppConstants.getAppId()}
 *   {
 *     "success": true,
 *     "data": {
 *       "enabled": 1,           // 开启插件更新 0/1
 *       "title":   "更新标题",   // 对应管理端「更新标题」
 *       "content": "更新内容",   // 对应管理端「更新内容」
 *       "url":     "https://...",// 对应管理端「更新链接」（APK 直链或下载页）
 *       "force":   0,           // 是否强制更新 0/1
 *       "version_code": 3,      // 可选：新版本 versionCode
 *       "version_name": "1.2"   // 可选：新版本号（展示用）
 *     }
 *   }
 * </pre>
 *
 * 显示规则：
 * <ol>
 *   <li>"开启插件更新" 为假 → 不弹；</li>
 *   <li>服务端给了 version_code 时，只有 version_code &gt; 本机 versionCode 才弹；</li>
 *   <li>非强制更新且该版本已被用户点过「稍后再说」→ 不再弹。</li>
 * </ol>
 */
public class UpdateManager {

    private static final String TAG = "UpdateManager";
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private static final String PREFS = "PluginUpdatePrefs";
    private static final String KEY_SKIP_VERSION = "skip_version_code";

    /** 更新信息（字段与最新一次服务端配置对应） */
    public static class UpdateInfo {
        public final String title;
        public final String content;
        public final String url;
        public final boolean force;
        public final int versionCode;
        public final String versionName;

        public UpdateInfo(String title, String content, String url, boolean force, int versionCode, String versionName) {
            this.title = title;
            this.content = content;
            this.url = url;
            this.force = force;
            this.versionCode = versionCode;
            this.versionName = versionName;
        }
    }

    /**
     * 检查更新。需要弹窗时弹窗（弹完/忽略后回调），不需要时直接回调。
     * onComplete 允许为 null。
     */
    public static void checkUpdate(final Activity activity, final Runnable onComplete) {
        if (activity == null) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }
        if (!NetworkManager.isNetworkAvailable(activity)) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchUpdate(activity, onComplete);
            }
        }).start();
    }

    private static void fetchUpdate(final Activity activity, final Runnable onComplete) {
        HttpURLConnection conn = null;
        try {
            String urlString = AppConstants.PLUGIN_UPDATE_URL + "?app_id=" + AppConstants.getAppId();
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            int code = conn.getResponseCode();
            if (code != 200) {
                Log.e(TAG, "HTTP " + code);
                postComplete(onComplete);
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(sb.toString());
            JSONObject data = json.optJSONObject("data");
            if (json.optBoolean("success") && data != null && isEnabled(data)) {
                final UpdateInfo info = parse(data);
                if (shouldShow(activity, info, getLocalVersionCode(activity))) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtils.showUpdateDialog(activity, info, onComplete);
                        }
                    });
                    return;
                }
            }
            postComplete(onComplete);        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "更新配置拉取失败: " + e.getMessage());
            postComplete(onComplete);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static UpdateInfo parse(JSONObject data) {
        String title = data.optString("title", "发现新版本");
        String content = data.optString("content", "");
        String url = data.optString("url", "");
        boolean force = optBool(data, "force") || optBool(data, "is_force")
                || optBool(data, "force_update") || optBool(data, "qiangzhi");
        int versionCode = data.optInt("version_code", data.optInt("versionCode", 0));
        String versionName = data.optString("version_name", data.optString("versionName", ""));
        return new UpdateInfo(title, content, url, force, versionCode, versionName);
    }

    /** 兼容 enabled / is_enabled / open / status 等字段名；都没给时，返回了 data 即视为开启 */
    private static boolean isEnabled(JSONObject data) {
        if (data.has("enabled")) {
            return optBool(data, "enabled");
        }
        if (data.has("is_enabled")) {
            return optBool(data, "is_enabled");
        }
        if (data.has("open")) {
            return optBool(data, "open");
        }
        if (data.has("status")) {
            return optBool(data, "status");
        }
        return true;
    }

    /** 兼容 1/0、true/false、"1"/"0" 多种写法 */
    private static boolean optBool(JSONObject data, String key) {
        Object value = data.opt(key);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        String s = String.valueOf(value).trim();
        return "1".equals(s) || "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s);
    }

    /**
     * 是否显示更新弹窗。
     * <p>按产品需求：只要管理端「开启更新插件」为真（即本方法被调用时 data 已通过 {@link #isEnabled}），
     * 就每次都要弹，不再比对版本号、也不受「稍后再说」影响。</p>
     */
    private static boolean shouldShow(Context context, UpdateInfo info, int localVersionCode) {
        return true;
    }

    /** 用户点「稍后再说」时记录忽略的版本（仅对带 version_code 的更新生效） */
    public static void markVersionSkipped(Context context, int versionCode) {
        if (context == null || versionCode <= 0) {
            return;
        }
        try {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .edit()
                    .putInt(KEY_SKIP_VERSION, versionCode)
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getSkippedVersion(Context context) {
        try {
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .getInt(KEY_SKIP_VERSION, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private static int getLocalVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    /** 打开更新链接：APK 直链或下载页都交给系统浏览器处理 */
    public static void openUpdateUrl(Activity activity, String url) {
        if (activity == null) {
            return;
        }
        if (url == null || url.trim().isEmpty()) {
            Toast.makeText(activity, "更新链接无效", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.trim()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "未找到可打开该链接的应用", Toast.LENGTH_SHORT).show();
        }
    }

    private static void postComplete(final Runnable onComplete) {
        if (onComplete == null) {
            return;
        }
        handler.post(onComplete);
    }
}