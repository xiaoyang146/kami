package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 空白载体界面。
 * 只加载 activity_main.xml，不做任何逻辑调用。
 * 卡密验证弹窗等能力由外部自行提取接入。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}