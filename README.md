# kami

卡密验证 Android 应用（`com.example.myapplication`）。

## 功能

- 启动时拉取远程公告（`NoticeManager`），有关闭弹窗后进入校验流程
- 卡密验证（`CardKeyManager`）：本地已保存卡密 → 静默自动登录；否则弹出输入框
- 环境检测：VPN / Xposed 检测，命中则退出
- 设备指纹 + 定时心跳校验（`DeviceInfoManager` / `CardKeyManager.checkKamiPeriodically`）

## 编译时字符串加密

`app/build.gradle` 内置 `generateEncryptedSources` 任务：

编译前扫描 `src/main/java` 下的所有 Java 源码，把 **URL** 和 **含中文的字符串字面量**
用 AES/GCM（密钥由 `Cryptor.KEY_SEED` 经 SHA-256 派生）加密，替换为
`com.example.myapplication.crypt.Cryptor.decrypt("...")` 调用，输出到
`app/build/generated/encrypted-src/main/java` 再参与编译。

因此仓库里的源码是明文，打出来的 APK 里是密文；且**每次构建密文都不同**（随机 IV）。

## 构建

本仓库已配置 GitHub Actions（`.github/workflows/android.yml`）：

- push 到 `main` 或手动 `workflow_dispatch` 触发
- 依次执行 `./gradlew assembleDebug assembleRelease`
- APK 作为 artifact 上传，并自动发布到 Release（tag `v1.0.<run_number>`）

本地构建：

```bash
./gradlew assembleDebug
# 产物：app/build/outputs/apk/debug/app-debug.apk
```

## 环境

| 项 | 值 |
| --- | --- |
| AGP | 8.0.0-alpha05 |
| Gradle | 7.5 |
| compileSdk / targetSdk | 33 |
| minSdk | 24 |
| JDK | 17（CI） |
| 依赖 | appcompat 1.4.1、material 1.5.0、okhttp 4.12.0 |

## 注意

- `local.properties` 含本机 SDK 路径，已在 `.gitignore` 中排除，不要提交。
- `app/src/main/java/com/example/myapplication/R.java` 是反编译产物，
  加密任务会跳过它，且 `sourceSets` 只编译加密副本，不会与 AGP 生成的 R 冲突。