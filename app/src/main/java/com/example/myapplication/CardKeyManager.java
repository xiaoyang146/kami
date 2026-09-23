package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.example.myapplication.CardKeyManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class CardKeyManager {
    private static final String KEY_SAVED_KAMI = "saved_kami";
    private static final String PREFS = "KamiPrefs";
    private static final String TAG = "CardKeyManager";
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final Handler periodicHandler = new Handler(Looper.getMainLooper());
    private static Runnable periodicCheckTask;
    private static boolean periodicCheckRunning = false;
    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();

    public static void validateKami(final Activity activity, String kamiCode, String deviceId, final boolean autoLogin) {
        Log.d(TAG, "验证卡密: " + kamiCode + ", 设备ID: " + deviceId);
        if (kamiCode == null || kamiCode.trim().isEmpty()) {
            handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CardKeyManager.lambda$validateKami$0(activity, autoLogin);
                }
            });
            return;
        }
        if (deviceId == null || deviceId.trim().isEmpty()) {
            handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    CardKeyManager.lambda$validateKami$1(activity, autoLogin);
                }
            });
            return;
        }
        FormBody formBody = new FormBody.Builder().add("kami_code", kamiCode).add("device_id", deviceId).build();
        Request request = new Request.Builder().url(AppConstants.VERIFY_KAMI_URL).post(formBody).build();
        client.newCall(request).enqueue(new AnonymousClass1(activity, autoLogin, kamiCode));
    }

    static /* synthetic */ void lambda$validateKami$0(Activity activity, boolean autoLogin) {
        Toast.makeText(activity, "卡密不能为空", Toast.LENGTH_SHORT).show();
        if (autoLogin) {
            showInputDialog(activity);
        }
    }

    static /* synthetic */ void lambda$validateKami$1(Activity activity, boolean autoLogin) {
        Toast.makeText(activity, "设备ID获取失败，请重启应用", Toast.LENGTH_SHORT).show();
        if (autoLogin) {
            showInputDialog(activity);
        }
    }

    /* renamed from: com.example.myapplication.CardKeyManager$1, reason: invalid class name */
    static class AnonymousClass1 implements Callback {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ boolean val$autoLogin;
        final /* synthetic */ String val$kamiCode;

        AnonymousClass1(Activity activity, boolean z, String str) {
            this.val$activity = activity;
            this.val$autoLogin = z;
            this.val$kamiCode = str;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, final IOException e) {
            Log.e(CardKeyManager.TAG, "网络请求失败", e);
            Handler handler = CardKeyManager.handler;
            final Activity activity = this.val$activity;
            final boolean z = this.val$autoLogin;
            handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    CardKeyManager.AnonymousClass1.lambda$onFailure$0(activity, e, z);
                }
            });
        }

        static /* synthetic */ void lambda$onFailure$0(Activity activity, IOException e, boolean autoLogin) {
            Toast.makeText(activity, "网络错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            if (autoLogin) {
                CardKeyManager.showInputDialog(activity);
            }
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, final Response response) throws IOException {
            String respBody = response.body() != null ? response.body().string() : HttpUrl.FRAGMENT_ENCODE_SET;
            Log.d(CardKeyManager.TAG, "响应: " + respBody);
            if (!response.isSuccessful()) {
                Handler handler = CardKeyManager.handler;
                final Activity activity = this.val$activity;
                final boolean z = this.val$autoLogin;
                handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        CardKeyManager.AnonymousClass1.lambda$onResponse$1(activity, response, z);
                    }
                });
                return;
            }
            CardKeyManager.handleResponse(this.val$activity, respBody, this.val$kamiCode, this.val$autoLogin);
        }

        static /* synthetic */ void lambda$onResponse$1(Activity activity, Response response, boolean autoLogin) {
            Toast.makeText(activity, "HTTP错误：" + response.code(), Toast.LENGTH_SHORT).show();
            if (autoLogin) {
                CardKeyManager.showInputDialog(activity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleResponse(final Activity activity, String response, String kamiCode, final boolean autoLogin) {
        try {
            JSONObject json = new JSONObject(response);
            boolean success = json.optBoolean("success");
            final String message = json.optString("message");
            if (!success) {
                handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        CardKeyManager.lambda$handleResponse$2(activity, message, autoLogin);
                    }
                });
                return;
            }
            JSONObject data = json.optJSONObject("data");
            if (data != null && data.optBoolean("valid")) {
                saveKami(activity, kamiCode);
                final String expireTime = data.optString("expire_time", HttpUrl.FRAGMENT_ENCODE_SET);
                int appIdInt = data.optInt("app_id", 1);
                final String appIdStr = String.valueOf(appIdInt);
                handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        CardKeyManager.lambda$handleResponse$5(expireTime, activity, appIdStr);
                    }
                });
                return;
            }
            handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    CardKeyManager.lambda$handleResponse$3(message, activity, autoLogin);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "解析响应失败", e);
            handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    CardKeyManager.lambda$handleResponse$6(activity, autoLogin);
                }
            });
        }
    }

    static /* synthetic */ void lambda$handleResponse$2(Activity activity, String message, boolean autoLogin) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        if (autoLogin) {
            saveKami(activity, HttpUrl.FRAGMENT_ENCODE_SET);
            showInputDialog(activity);
        }
    }

    static /* synthetic */ void lambda$handleResponse$3(String errorMsg, Activity activity, boolean autoLogin) {
        if (errorMsg != null && (errorMsg.contains("设备ID不匹配") || errorMsg.contains("已绑定其他设备"))) {
            Toast.makeText(activity, "设备id已绑定,请先解绑", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "卡密无效或已到期", Toast.LENGTH_SHORT).show();
        }
        if (autoLogin) {
            saveKami(activity, HttpUrl.FRAGMENT_ENCODE_SET);
            showInputDialog(activity);
        }
    }

    static /* synthetic */ void lambda$handleResponse$5(String expireTime, Activity activity, String appIdStr) {
        if (DialogUtils.dialog != null && DialogUtils.dialog.isShowing()) {
            DialogUtils.dialog.dismiss();
        }
        String toastMsg = "登录成功";
        if (!expireTime.isEmpty() && !"永久有效".equals(expireTime)) {
            toastMsg = "登录成功，到期：" + expireTime;
        } else if ("永久有效".equals(expireTime)) {
            toastMsg = "登录成功，永久有效";
        }
        Toast.makeText(activity, toastMsg, Toast.LENGTH_LONG).show();
        startPeriodicValidationCheck(activity);
        NoticeManager.fetchNoticesWithAppId(activity, appIdStr, new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CardKeyManager.lambda$handleResponse$4();
            }
        });
    }

    static /* synthetic */ void lambda$handleResponse$4() {
    }

    static /* synthetic */ void lambda$handleResponse$6(Activity activity, boolean autoLogin) {
        Toast.makeText(activity, "服务端响应格式错误", Toast.LENGTH_SHORT).show();
        if (autoLogin) {
            showInputDialog(activity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showInputDialog(final Activity activity) {
        stopPeriodicValidationCheck();
        handler.post(new Runnable() { // from class: com.example.myapplication.CardKeyManager$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                DialogUtils.showValidationDialog(activity);
            }
        });
    }

    public static void saveKami(Context context, String kami) {
        context.getSharedPreferences(PREFS, 0).edit().putString(KEY_SAVED_KAMI, kami).apply();
    }

    public static String getSavedKami(Context context) {
        return context.getSharedPreferences(PREFS, 0).getString(KEY_SAVED_KAMI, HttpUrl.FRAGMENT_ENCODE_SET);
    }

    public static void startPeriodicValidationCheck(final Activity activity) {
        stopPeriodicValidationCheck();
        periodicCheckRunning = true;
        periodicCheckTask = new Runnable() {
            @Override
            public void run() {
                if (!periodicCheckRunning) return;
                String kami = getSavedKami(activity);
                String deviceId = DeviceInfoManager.getDeviceId(activity);
                if (!kami.isEmpty() && !deviceId.isEmpty()) {
                    checkKamiPeriodically(activity, kami, deviceId);
                }
                periodicHandler.postDelayed(this, 30000);
            }
        };
        periodicHandler.postDelayed(periodicCheckTask, 30000);
    }

    public static void stopPeriodicValidationCheck() {
        periodicCheckRunning = false;
        if (periodicCheckTask != null) {
            periodicHandler.removeCallbacks(periodicCheckTask);
            periodicCheckTask = null;
        }
    }

    private static void checkKamiPeriodically(final Activity activity, String kamiCode, String deviceId) {
        FormBody formBody = new FormBody.Builder()
                .add("kami_code", kamiCode)
                .add("device_id", deviceId)
                .build();
        Request request = new Request.Builder()
                .url(AppConstants.VERIFY_KAMI_URL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 网络错误，跳过本次检测
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String respBody = response.body() != null ? response.body().string() : HttpUrl.FRAGMENT_ENCODE_SET;
                    JSONObject json = new JSONObject(respBody);
                    boolean success = json.optBoolean("success");
                    if (!success) {
                        handleInvalidKey(activity, "卡密不存在");
                        return;
                    }
                    JSONObject data = json.optJSONObject("data");
                    if (data == null || !data.optBoolean("valid")) {
                        handleInvalidKey(activity, "卡密已到期");
                    }
                    // data.valid == true → 卡密仍有效，不做任何事
                } catch (Exception e) {
                    Log.e(TAG, "定时检测解析失败", e);
                }
            }
        });
    }

    private static void handleInvalidKey(final Activity activity, final String message) {
        stopPeriodicValidationCheck();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                showInputDialog(activity);
            }
        });
    }
}
