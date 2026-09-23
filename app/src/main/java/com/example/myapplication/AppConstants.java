package com.example.myapplication;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class AppConstants {
    private static String APP_ID = "1";
    public static final String BASE_URL = "https://ok1666.cn/APPyingyon";
    public static final String NOTICE_URL = "https://ok1666.cn/APPyingyon/gonggao/get_gonggao.php";
    public static final String VERIFY_KAMI_URL = "https://ok1666.cn/APPyingyon/kami/verify_kami.php";
    /* 插件更新配置读取接口：配置由 Cloud-APP「插件更新」页面保存到服务端 */
    public static final String GENGXIN_URL = "https://ok1666.cn/APPyingyon/gengxin/get_gengxin.php";

    public static String getAppId() {
        return APP_ID;
    }

    public static void setAppId(String appId) {
        APP_ID = appId;
    }
}
