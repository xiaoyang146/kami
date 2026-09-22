package com.example.myapplication;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class AppConstants {
    private static String APP_ID = "1";
    public static final String BASE_URL = "https://ok1666.cn/APPyingyon";
    public static final String NOTICE_URL = "https://ok1666.cn/APPyingyon/gonggao/get_gonggao.php";
    public static final String VERIFY_KAMI_URL = "https://ok1666.cn/APPyingyon/kami/verify_kami.php";

    public static String getAppId() {
        return APP_ID;
    }

    public static void setAppId(String appId) {
        APP_ID = appId;
    }
}
