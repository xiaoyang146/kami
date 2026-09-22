package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.example.myapplication.HttpUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class HttpUtil {
    private static final String TAG = "HttpUtil";
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public interface ErrorCallback {
        void onError(String str);
    }

    public interface ResponseCallback {
        void onResponse(String str);
    }

    public static void postFormData(final String urlString, final String formData, final ResponseCallback responseCallback, final ErrorCallback errorCallback) {
        new Thread(new Runnable() { // from class: com.example.myapplication.HttpUtil$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                HttpUtil.lambda$postFormData$3(urlString, formData, responseCallback, errorCallback);
            }
        }).start();
    }

    static /* synthetic */ void lambda$postFormData$3(String urlString, String formData, final ResponseCallback responseCallback, final ErrorCallback errorCallback) {
        try {
            Log.d(TAG, "Sending POST form data to: " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream os = conn.getOutputStream();
            try {
                os.write(formData.getBytes("UTF-8"));
                if (os != null) {
                    os.close();
                }
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);
                if (responseCode == 200) {
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder response = new StringBuilder();
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        } else {
                            response.append(line);
                        }
                    }
                    br.close();
                    is.close();
                    final String responseStr = response.toString();
                    Log.d(TAG, "Response: " + responseStr);
                    handler.post(new Runnable() { // from class: com.example.myapplication.HttpUtil$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            responseCallback.onResponse(responseStr);
                        }
                    });
                } else {
                    final String errorMsg = "HTTP error: " + responseCode;
                    Log.e(TAG, errorMsg);
                    handler.post(new Runnable() { // from class: com.example.myapplication.HttpUtil$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            errorCallback.onError(errorMsg);
                        }
                    });
                }
                conn.disconnect();
            } finally {
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in postFormData: " + e.getMessage());
            e.printStackTrace();
            handler.post(new Runnable() { // from class: com.example.myapplication.HttpUtil$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    errorCallback.onError(e.getMessage());
                }
            });
        }
    }
}