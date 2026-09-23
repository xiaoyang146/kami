package com.example.myapplication;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class AppConstants {
    private static String APP_ID = "1";
    public static final String BASE_URL = "https://ok1666.cn/APPyingyon";
    public static final String NOTICE_URL = "https://ok1666.cn/APPyingyon/gonggao/get_gonggao.php";
    public static final String VERIFY_KAMI_URL = "https://ok1666.cn/APPyingyon/kami/verify_kami.php";
    /**
     * 插件更新配置接口（数据源：Cloud-APP「插件更新」页面保存的配置）。
     *
     * 完整链路：
     *   Cloud-APP「插件更新」页 → save_gengxin.php（保存标题/内容/链接/开关/强制）
     *   → kami 启动并检测到网络 → 本接口 → 弹出更新对话框 → 点「立即更新」用系统浏览器打开链接
     *
     * 请求：?app_id={AppConstants.getAppId()}（找不到该应用配置时服务端回退到 app_id=0 的「所有应用」全局配置）
     */
    public static final String PLUGIN_UPDATE_URL = "https://ok1666.cn/APPyingyon/gengxin/get_gengxin.php";

    public static String getAppId() {
        return APP_ID;
    }

    public static void setAppId(String appId) {
        APP_ID = appId;
    }
}
