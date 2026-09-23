# 主入口 — 注入后的 APK 通过此方法调用 xiao.dex
-keep class com.example.myapplication.MacUtils {
    public static void showCardKeyValidationScreen(android.app.Activity);
}

# 运行时字符串解密工具
-keep class com.example.myapplication.crypt.Cryptor { *; }

# AppConstants 的静态字段被 CardKeyManager 引用，保持字段名
-keepclassmembers class com.example.myapplication.AppConstants {
    public static java.lang.String BASE_URL;
    public static java.lang.String NOTICE_URL;
    public static java.lang.String VERIFY_KAMI_URL;
    public static java.lang.String getAppId();
}

# ── 自定义混淆命名 ──

# 用 Cloud/Cloud1/Cloud2... 替代默认的 a/b/c
-obfuscationdictionary obfuscation-dict.txt
-classobfuscationdictionary obfuscation-dict.txt

# 所有混淆后的类放入单包，压缩结构
-repackageclasses c
