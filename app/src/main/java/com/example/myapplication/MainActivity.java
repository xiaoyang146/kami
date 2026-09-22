package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 程序入口界面（空白首页）。
 *
 * 启动流程：
 *   1. 加载空白布局 activity_main.xml
 *   2. 拉取公告（有公告则弹出公告框，关闭后继续；无公告/失败则直接继续）
 *   3. 进入卡密校验：本地有已保存卡密 → 静默自动验证；没有 → 弹出卡密输入框
 */
public class MainActivity extends AppCompatActivity {

    private boolean kamiFlowStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoticeManager.fetchNotices(this, new Runnable() {
            @Override
            public void run() {
                startKamiValidation();
            }
        });
    }

    /** 卡密校验入口 */
    private void startKamiValidation() {
        if (kamiFlowStarted) {
            return;
        }
        kamiFlowStarted = true;

        if (!NetworkManager.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，请检查网络后重试", Toast.LENGTH_LONG).show();
        }

        String savedKami = CardKeyManager.getSavedKami(this);
        if (savedKami != null && !savedKami.isEmpty()) {
            // 有已保存的卡密：静默自动登录
            String deviceId = DeviceInfoManager.getDeviceId(this);
            CardKeyManager.validateKami(this, savedKami, deviceId, true);
        } else {
            // 没有卡密：弹出输入框
            CardKeyManager.showInputDialog(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CardKeyManager.stopPeriodicValidationCheck();
    }
}