package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.NoticeManager;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class DialogUtils {
    public static AlertDialog dialog;

    public static void showNoticeDialog(Activity activity, NoticeManager.Notice notice, final Runnable onDismiss) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(1);
        layout.setPadding(50, 40, 50, 30);
        layout.setBackground(createRoundRectDrawable(-1, 20.0f));
        TextView titleView = new TextView(activity);
        titleView.setText(notice.title);
        titleView.setTextSize(20.0f);
        titleView.setTypeface(null, 1);
        titleView.setTextColor(-13877680);
        titleView.setGravity(17);
        titleView.setPadding(0, 0, 0, 20);
        TextView contentView = new TextView(activity);
        contentView.setText(notice.content);
        contentView.setTextSize(16.0f);
        contentView.setTextColor(-13350562);
        contentView.setPadding(20, 20, 20, 30);
        contentView.setGravity(17);
        ScrollView scrollView = new ScrollView(activity);
        scrollView.addView(contentView);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        scrollParams.setMargins(0, 10, 0, 20);
        Button confirmBtn = new Button(activity);
        confirmBtn.setText("确认");
        confirmBtn.setTextSize(16.0f);
        confirmBtn.setTextColor(-1);
        confirmBtn.setBackground(createRoundRectDrawable(-13330213, 30.0f));
        confirmBtn.setPadding(0, 15, 0, 15);
        confirmBtn.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DialogUtils.lambda$showNoticeDialog$0(onDismiss, view);
            }
        });
        layout.addView(titleView);
        layout.addView(scrollView, scrollParams);
        layout.addView(confirmBtn);
        AlertDialog create = new AlertDialog.Builder(activity).create();
        dialog = create;
        create.setView(layout);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (UIUtils.getScreenWidth(activity) * 0.85d);
            params.height = (int) (UIUtils.getScreenHeight(activity) * 0.6d);
            window.setAttributes(params);
        }
        dialog.show();
    }

    static /* synthetic */ void lambda$showNoticeDialog$0(Runnable onDismiss, View v) {
        AlertDialog alertDialog = dialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            dialog.dismiss();
        }
        onDismiss.run();
    }

    public static void showValidationDialog(final Activity activity) {
        if (NetworkManager.isVpnActive(activity)) {
            Toast.makeText(activity, "检测到VPN已开启，即将退出", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DialogUtils.lambda$showValidationDialog$1(activity);
                }
            }, 2000L);
            return;
        }
        if (isXposedInstalled()) {
            Toast.makeText(activity, "检测到XP框架，即将退出", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DialogUtils.lambda$showValidationDialog$2(activity);
                }
            }, 2000L);
            return;
        }
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(50, 40, 50, 30);
        linearLayout.setBackground(createRoundRectDrawable(-1, 20.0f));
        linearLayout.setGravity(17);
        TextView title = new TextView(activity);
        title.setText("卡密验证系统");
        title.setTextSize(22.0f);
        title.setTextColor(-13877680);
        title.setTypeface(null, 1);
        title.setGravity(17);
        title.setPadding(0, 0, 0, 15);
        TextView contact = new TextView(activity);
        contact.setText("联系QQ：2502660988");
        contact.setTextSize(14.0f);
        contact.setTextColor(-8418163);
        contact.setGravity(17);
        contact.setPadding(0, 0, 0, 30);
        final EditText kamiInput = new EditText(activity);
        kamiInput.setHint("请输入卡密");
        kamiInput.setInputType(1);
        kamiInput.setTextSize(16.0f);
        kamiInput.setHintTextColor(-6969946);
        kamiInput.setTextColor(-13877680);
        kamiInput.setBackground(createRoundRectDrawable(-1249039, 10.0f));
        kamiInput.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(-1, -2);
        inputParams.setMargins(0, 0, 0, 30);
        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setOrientation(0);
        buttonLayout.setGravity(17);
        buttonLayout.setWeightSum(3.0f);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        btnParams.setMargins(10, 0, 10, 0);
        Button exitBtn = createStyledButton(activity, "退出", -1618884);
        exitBtn.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                activity.finish();
            }
        });
        Button buyBtn = createStyledButton(activity, "购卡", -13710223);
        buyBtn.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DialogUtils.lambda$showValidationDialog$4(activity, view);
            }
        });
        Button loginBtn = createStyledButton(activity, "登录", -13330213);
        loginBtn.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DialogUtils.lambda$showValidationDialog$5(kamiInput, activity, view);
            }
        });
        buttonLayout.addView(exitBtn, btnParams);
        buttonLayout.addView(buyBtn, btnParams);
        buttonLayout.addView(loginBtn, btnParams);
        linearLayout.addView(title);
        linearLayout.addView(contact);
        linearLayout.addView(kamiInput, inputParams);
        linearLayout.addView(buttonLayout);
        AlertDialog create = new AlertDialog.Builder(activity).create();
        dialog = create;
        create.setView(linearLayout);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (UIUtils.getScreenWidth(activity) * 0.85d);
            window.setAttributes(params);
        }
        dialog.show();
    }

    static /* synthetic */ void lambda$showValidationDialog$1(Activity activity) {
        activity.finish();
        System.exit(0);
    }

    static /* synthetic */ void lambda$showValidationDialog$2(Activity activity) {
        activity.finish();
        System.exit(0);
    }

    static /* synthetic */ void lambda$showValidationDialog$4(Activity activity, View v) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://ok1666.cn"));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "无法打开浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    static /* synthetic */ void lambda$showValidationDialog$5(EditText kamiInput, Activity activity, View v) {
        String kamiCode = kamiInput.getText().toString().trim();
        if (kamiCode.isEmpty()) {
            Toast.makeText(activity, "请输入卡密", Toast.LENGTH_SHORT).show();
        } else {
            CardKeyManager.validateKami(activity, kamiCode, DeviceInfoManager.getDeviceId(activity), false);
        }
    }

    public static Button createStyledButton(Context context, String text, int color) {
        Button button = new Button(context);
        button.setText(text);
        button.setTextSize(16.0f);
        button.setTextColor(-1);
        button.setBackground(createRoundRectDrawable(color, 30.0f));
        button.setPadding(0, 15, 0, 15);
        button.setTypeface(null, 1);
        return button;
    }

    public static GradientDrawable createRoundRectDrawable(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    /** 打开更新链接（直接调用系统浏览器访问） */
    public static void openUpdateUrl(Activity activity, String url) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "无法打开浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 插件更新对话框（标题 / 内容 / 更新链接均来自服务端配置）。
     * 非强制：可「稍后再说」或返回键取消，取消后继续 onNext；
     * 强制：不可任何形式取消，只有「更新」按钮，点击直接打开系统浏览器。
     */
    public static void showUpdateDialog(final Activity activity, String updateTitle, String updateContent,
                                        final String updateUrl, final boolean forceUpdate, final Runnable onNext) {
        final boolean[] nextCalled = {false};
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(1);
        layout.setPadding(50, 40, 50, 30);
        layout.setBackground(createRoundRectDrawable(-1, 20.0f));
        TextView titleView = new TextView(activity);
        titleView.setText(updateTitle);
        titleView.setTextSize(20.0f);
        titleView.setTypeface(null, 1);
        titleView.setTextColor(-13877680);
        titleView.setGravity(17);
        titleView.setPadding(0, 0, 0, 20);
        TextView contentView = new TextView(activity);
        contentView.setText(updateContent);
        contentView.setTextSize(16.0f);
        contentView.setTextColor(-13350562);
        contentView.setPadding(20, 20, 20, 30);
        contentView.setGravity(17);
        ScrollView scrollView = new ScrollView(activity);
        scrollView.addView(contentView);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        scrollParams.setMargins(0, 10, 0, 20);
        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setOrientation(0);
        buttonLayout.setGravity(17);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        btnParams.setMargins(10, 0, 10, 0);
        final AlertDialog updateDialog = new AlertDialog.Builder(activity).create();
        if (!forceUpdate) {
            Button laterBtn = createStyledButton(activity, "稍后再说", -1618884);
            laterBtn.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    if (updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    if (!nextCalled[0]) {
                        nextCalled[0] = true;
                        if (onNext != null) {
                            onNext.run();
                        }
                    }
                }
            });
            buttonLayout.addView(laterBtn, btnParams);
        } else {
            buttonLayout.setWeightSum(1.0f);
        }
        Button updateBtn = createStyledButton(activity, "更新", -13330213);
        updateBtn.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                openUpdateUrl(activity, updateUrl);
                if (!forceUpdate) {
                    if (updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    if (!nextCalled[0]) {
                        nextCalled[0] = true;
                        if (onNext != null) {
                            onNext.run();
                        }
                    }
                }
            }
        });
        buttonLayout.addView(updateBtn, btnParams);
        layout.addView(titleView);
        layout.addView(scrollView, scrollParams);
        layout.addView(buttonLayout);
        updateDialog.setView(layout);
        // 强制更新：返回键 / 点击外部 / 取消 全部无效
        updateDialog.setCancelable(!forceUpdate);
        updateDialog.setCanceledOnTouchOutside(false);
        if (!forceUpdate) {
            updateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.example.myapplication.DialogUtils$$ExternalSyntheticLambda8
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    if (!nextCalled[0]) {
                        nextCalled[0] = true;
                        if (onNext != null) {
                            onNext.run();
                        }
                    }
                }
            });
        }
        Window window = updateDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (UIUtils.getScreenWidth(activity) * 0.85d);
            params.height = (int) (UIUtils.getScreenHeight(activity) * 0.6d);
            window.setAttributes(params);
        }
        updateDialog.show();
    }

    private static boolean isXposedInstalled() {
        try {
            Class.forName("de.robv.android.xposed.XposedBridge");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
