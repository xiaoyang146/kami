package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.URL;

public class DialogUtils {
    private static final String BUY_URL = "https://ok1666.cn/cloud/";
    private static final String IMG_CART = "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";
    private static final String IMG_CHAT = "iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAYAAADimHc4AAAD5UlEQVR4nO3dzW4bZRTG8ee8M+MP6pJWQiRV2ojSJWJVNkgI+QpglQW9AJYsiVSBXCNRKXRXuICuENBVr6BZIIHU7hDdBaokpUkVQUit1B8z72ExpuUjlWfssU9tP7+dLY/GOf9x7GRxDBARERER0byRkY5uqAM2HFAv5tlMnQ0AdY+m+MmeV1WwqsFkT/oCW9UAqkNdzGHuIxrqIOIBJGfWHqyIVN7WpHcB0JKIqA75RKbFs59RuhJEm6rtHx6uyxYE6WxyvhryDat/glc/2lwMT5z+TFU/cFHlJCQc9ZfZ9FEAGsP32o8F8k189Menj65f2MsbIfvY/h7+2taboavdklLtvG8fAD5J+k9nHgmcC1zlNLT7+L7zvfd21pd+yhMhW4CGOgBYbO2+IlF4x4XVFe0c9iASAjJv1/5/qEI1lvLLke8dbWsleWsPS/sAkCWCy3SOexA0xYuTq0F5oT98F3H4ACACcZF2DntB5dQ5OdKraIrHvWwX9+AAjYbDTUnOXt5fhsgl3/7Tp1c+/YtImM4muLT88fZZ3JQEjcbA+WZ4BdQdAHR78buuXKvCx8or/zgi8LG6cq0aI3wnva9eSAAAQOBwXiRUzO8bbhYqEqpTeT29WR94QLb3AADwWoLM3YfN/AQCaCnrw7MHoLFgAGMMYIwBjDGAMQYwxgDGGMAYAxhjAGMMYIwBjDGAMQYwxgDGGMAYAxhjAGMMYIwBjDGAMQYwxgDGGMAYAxhjAGMMYIwBjDGAMQYwxgDGGMAYAxhjAGMMYIwBjDGAMQYwxgDGGMAYAxhjAGMMYIwBjDFA0RQKSDfrwxmgWKIaixf9Jb25MfAABiiMKlwovtN6Ej6Jv0/v2xi4M46rx4qiGrvqQpQc7X/94MtzO1jVAE1JBh3GACN7tjUxaR9s60tyub9lMtNmsckEUMQzuulM4ILAVU9F2m39GqL9/k5z5VGevaHjD6DqXWUhhJuxVdNPN+d2DrXT+nbYzbljDKAKFZXSCZd0Dm844L5CXfoxbXo93R0triNBuAlt//jb+vIWgKF2R48pgCrgElddCJP2wZXdL5aa4znPC2JVA3wH319qnssYAvSHX/nH8D+8GwEAzlyc6qv/f96A4ucriqYkw+6TLDjAMcNv3A7RvBgDMlvDL0iBf4g9b/j1hMN/voICcPjDKiAAhz+KEQNw+KMaIQCHX4QhA3D4RckfQMDhFyhnAIVCPIdfnMwBVFSh8K5UK3P4xcn+ClCnrlp2vtv6hMMvTuZ/RSj0ZO/3va92ry19zuFPVPrVhItrD18D0P9Osdn+ukKiY8z4l3QSERERERFNyl+oJcs6B707TQAAAABJRU5ErkJggg==";
    private static final String IMG_LOCK = "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";
    private static final String IMG_PACKAGE = "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";
    private static final String QQ_NUMBER = "479779023";
    public static AlertDialog dialog;
    private static TextView feedbackView;
    private static View statusDot;
    /** 待显示到「立即验证」按钮下方空白处的提示（弹窗重建后自动填充） */
    private static String pendingFeedback = null;

    public static void showNoticeDialog(Activity activity, NoticeManager.Notice notice, final Runnable runnable) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(50, 40, 50, 30);
        linearLayout.setBackground(createRoundRectDrawable(-1, 20.0f));
        TextView textView = new TextView(activity);
        textView.setText(notice.title);
        textView.setTextSize(20.0f);
        textView.setTypeface(null, 1);
        textView.setTextColor(-13877680);
        textView.setGravity(17);
        textView.setPadding(0, 0, 0, 20);
        TextView textView2 = new TextView(activity);
        textView2.setText(notice.content);
        textView2.setTextSize(16.0f);
        textView2.setTextColor(-13350562);
        textView2.setPadding(20, 20, 20, 30);
        textView2.setGravity(17);
        ScrollView scrollView = new ScrollView(activity);
        scrollView.addView(textView2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        layoutParams.setMargins(0, 10, 0, 20);
        Button button = new Button(activity);
        button.setText("确认");
        button.setTextSize(16.0f);
        button.setTextColor(-1);
        button.setBackground(createRoundRectDrawable(-13330213, 30.0f));
        button.setPadding(0, 15, 0, 15);
        button.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DialogUtils.dialog != null && DialogUtils.dialog.isShowing()) {
                    DialogUtils.dialog.dismiss();
                }
                runnable.run();
            }
        });
        linearLayout.addView(textView);
        linearLayout.addView(scrollView, layoutParams);
        linearLayout.addView(button);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(activity).create();
        dialog = alertDialogCreate;
        alertDialogCreate.setView(linearLayout);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = (int) (((double) UIUtils.getScreenWidth(activity)) * 0.85d);
            attributes.height = (int) (((double) UIUtils.getScreenHeight(activity)) * 0.6d);
            window.setAttributes(attributes);
        }
        dialog.show();
    }

    public static void showValidationDialog(final Activity activity) {
        if (NetworkManager.isVpnActive(activity)) {
            Toast.makeText(activity, "检测到VPN已开启，即将退出", 1).show();
            new Handler().postDelayed(new Runnable() {
                @Override // java.lang.Runnable
                public void run() {
                    activity.finish();
                    System.exit(0);
                }
            }, 2000L);
            return;
        }
        if (isXposedInstalled()) {
            Toast.makeText(activity, "检测到XP框架，即将退出", 0).show();
            new Handler().postDelayed(new Runnable() {
                @Override // java.lang.Runnable
                public void run() {
                    activity.finish();
                    System.exit(0);
                }
            }, 2000L);
            return;
        }
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        linearLayout.setBackground(createRoundRectDrawable(-1, dp(activity, 20.0f)));
        LinearLayout linearLayout2 = new LinearLayout(activity);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(17);
        linearLayout2.setPadding(dp(activity, 20.0f), dp(activity, 20.0f), dp(activity, 20.0f), dp(activity, 16.0f));
        LinearLayout linearLayout3 = new LinearLayout(activity);
        linearLayout3.setGravity(17);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-1);
        gradientDrawable.setStroke(dp(activity, 2.0f), Color.parseColor("#BFDBFE"));
        linearLayout3.setBackground(gradientDrawable);
        ImageView imageView = new ImageView(activity);
        Bitmap bitmapDecodeBase64Image = decodeBase64Image(IMG_PACKAGE);
        if (bitmapDecodeBase64Image != null) {
            imageView.setImageBitmap(toCircularBitmap(bitmapDecodeBase64Image));
        }
        linearLayout3.addView(imageView, new LinearLayout.LayoutParams(dp(activity, 44.0f), dp(activity, 44.0f)));
        linearLayout2.addView(linearLayout3, new LinearLayout.LayoutParams(dp(activity, 50.0f), dp(activity, 50.0f)));
        loadQQAvatar(activity, imageView);
        LinearLayout linearLayout4 = new LinearLayout(activity);
        linearLayout4.setOrientation(1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(dp(activity, 12.0f), 0, 0, 0);
        TextView textView = new TextView(activity);
        textView.setText("【请输入卡密🎉】");
        textView.setTextSize(20.0f);
        textView.setTextColor(Color.parseColor("#1A1A2E"));
        textView.setTypeface(null, 1);
        TextView textView2 = new TextView(activity);
        textView2.setText("👉·一机一码，通过后进入应用！🤤");
        textView2.setTextSize(14.0f);
        textView2.setTextColor(Color.parseColor("#888888"));
        linearLayout4.addView(textView);
        linearLayout4.addView(textView2);
        linearLayout2.addView(linearLayout4, layoutParams);
        linearLayout.addView(linearLayout2);
        linearLayout.addView(createDivider(activity, "#F0F0F5"));
        LinearLayout linearLayout5 = new LinearLayout(activity);
        linearLayout5.setOrientation(1);
        linearLayout5.setPadding(dp(activity, 20.0f), dp(activity, 22.0f), dp(activity, 20.0f), dp(activity, 18.0f));
        LinearLayout linearLayout6 = new LinearLayout(activity);
        linearLayout6.setOrientation(0);
        linearLayout6.setGravity(16);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(1);
        gradientDrawable2.setColor(Color.parseColor("#B8E0FF"));
        gradientDrawable2.setStroke(dp(activity, 2.0f), Color.parseColor("#3490F3"));
        LinearLayout linearLayout7 = new LinearLayout(activity);
        linearLayout7.setBackground(gradientDrawable2);
        statusDot = linearLayout7;
        startStatusBreathing(activity, false);
        linearLayout6.addView(linearLayout7, new LinearLayout.LayoutParams(dp(activity, 14.0f), dp(activity, 14.0f)));
        TextView textView3 = new TextView(activity);
        textView3.setText("输入卡密");
        textView3.setTextSize(17.0f);
        textView3.setTextColor(Color.parseColor("#222222"));
        textView3.setTypeface(null, 1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.setMargins(dp(activity, 10.0f), 0, 0, 0);
        linearLayout6.addView(textView3, layoutParams2);
        linearLayout5.addView(linearLayout6, new LinearLayout.LayoutParams(-1, -2));
        final EditText editText = new EditText(activity);
        editText.setHint("请输入您的卡密");
        editText.setInputType(1);
        editText.setTextSize(16.0f);
        editText.setTextColor(Color.parseColor("#1A1A2E"));
        editText.setHintTextColor(Color.parseColor("#B0B8C8"));
        GradientDrawable gradientDrawableCreateRoundRectDrawable = createRoundRectDrawable(Color.parseColor("#FAFBFF"), dp(activity, 12.0f));
        gradientDrawableCreateRoundRectDrawable.setStroke((int) (activity.getResources().getDisplayMetrics().density * 1.5f), Color.parseColor("#E2E8F0"));
        editText.setBackground(gradientDrawableCreateRoundRectDrawable);
        editText.setPadding(dp(activity, 16.0f), dp(activity, 14.0f), dp(activity, 16.0f), dp(activity, 14.0f));
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, dp(activity, 54.0f));
        layoutParams3.setMargins(0, dp(activity, 12.0f), 0, 0);
        linearLayout5.addView(editText, layoutParams3);
        LinearLayout linearLayout8 = new LinearLayout(activity);
        linearLayout8.setOrientation(0);
        linearLayout8.setGravity(16);
        ImageView imageView2 = new ImageView(activity);
        imageView2.setImageBitmap(decodeBase64Image(IMG_LOCK));
        linearLayout8.addView(imageView2, new LinearLayout.LayoutParams(dp(activity, 20.0f), dp(activity, 20.0f)));
        TextView textView4 = new TextView(activity);
        textView4.setText("👉请点击下方按钮购买卡密👇");
        textView4.setTextSize(14.0f);
        textView4.setTextColor(Color.parseColor("#7A7F93"));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams4.setMargins(dp(activity, 6.0f), 0, 0, 0);
        linearLayout8.addView(textView4, layoutParams4);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams5.setMargins(0, dp(activity, 14.0f), 0, dp(activity, 22.0f));
        linearLayout5.addView(linearLayout8, layoutParams5);
        Button button = new Button(activity);
        button.setText("立即验证 →");
        button.setTextSize(19.0f);
        button.setTextColor(-1);
        button.setTypeface(null, 1);
        button.setBackground(createGradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#1B6EF7"), Color.parseColor("#27A2FF")}, dp(activity, 14.0f)));
        button.setPadding(0, dp(activity, 17.0f), 0, dp(activity, 17.0f));
        applyClickEffect(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String strTrim = editText.getText().toString().trim();
                if (strTrim.isEmpty()) {
                    DialogUtils.showValidationFeedback(activity, "请输入卡密");
                } else {
                    CardKeyManager.validateKami(activity, strTrim, DeviceInfoManager.getDeviceId(activity), false);
                }
            }
        });
        linearLayout5.addView(button, new LinearLayout.LayoutParams(-1, dp(activity, 60.0f)));
        feedbackView = new TextView(activity);
        feedbackView.setGravity(17);
        feedbackView.setTextSize(14.0f);
        feedbackView.setPadding(dp(activity, 14.0f), dp(activity, 10.0f), dp(activity, 14.0f), dp(activity, 10.0f));
        feedbackView.setVisibility(4);
        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams6.setMargins(0, dp(activity, 12.0f), 0, 0);
        linearLayout5.addView(feedbackView, layoutParams6);
        linearLayout.addView(linearLayout5);
        linearLayout.addView(createDivider(activity, "#F0F0F5"));
        LinearLayout linearLayout9 = new LinearLayout(activity);
        linearLayout9.setOrientation(1);
        linearLayout9.setGravity(1);
        linearLayout9.setPadding(dp(activity, 20.0f), dp(activity, 16.0f), dp(activity, 20.0f), dp(activity, 24.0f));
        LinearLayout linearLayout10 = new LinearLayout(activity);
        linearLayout10.setOrientation(0);
        linearLayout10.setGravity(17);
        linearLayout10.addView(buildFooterLink(activity, IMG_CART, "购买卡密", new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DialogUtils.openBuyPage(activity);
            }
        }));
        linearLayout10.addView(new View(activity), new LinearLayout.LayoutParams(dp(activity, 40.0f), 1));
        linearLayout10.addView(buildFooterLink(activity, IMG_CHAT, "联系作者", new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DialogUtils.openQQProfile(activity);
            }
        }));
        linearLayout9.addView(linearLayout10);
        TextView textView5 = new TextView(activity);
        textView5.setText("👉未验证前无法进入应用！🙊");
        textView5.setTextSize(14.0f);
        textView5.setTextColor(Color.parseColor("#A0A8B8"));
        textView5.setGravity(17);
        LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams7.setMargins(0, dp(activity, 10.0f), 0, 0);
        linearLayout9.addView(textView5, layoutParams7);
        linearLayout.addView(linearLayout9);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(activity).create();
        dialog = alertDialogCreate;
        alertDialogCreate.setView(linearLayout);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setWindowAnimations(0);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = (int) (((double) UIUtils.getScreenWidth(activity)) * 0.9d);
            window.setAttributes(attributes);
        }
        dialog.show();
        // 弹窗建好后，把之前失败的提示填到「立即验证」按钮下方
        applyPendingFeedback();
    }

    private static LinearLayout buildFooterLink(Activity activity, String str, String str2, View.OnClickListener onClickListener) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setClickable(true);
        ImageView imageView = new ImageView(activity);
        imageView.setImageBitmap(decodeBase64Image(str));
        linearLayout.addView(imageView, new LinearLayout.LayoutParams(dp(activity, 22.0f), dp(activity, 22.0f)));
        TextView textView = new TextView(activity);
        textView.setText(str2);
        textView.setTextSize(16.0f);
        textView.setTextColor(Color.parseColor("#1976E8"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(dp(activity, 6.0f), 0, 0, 0);
        linearLayout.addView(textView, layoutParams);
        applyClickEffect(linearLayout);
        linearLayout.setOnClickListener(onClickListener);
        return linearLayout;
    }

    private static View createDivider(Activity activity, String str) {
        View view = new View(activity);
        view.setBackgroundColor(Color.parseColor(str));
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, dp(activity, 1.0f)));
        return view;
    }

    public static void showValidationFeedback(Activity activity, String str) {
        if (str == null || str.trim().isEmpty()) {
            str = "验证失败";
        }
        // 弹窗未显示时先暂存，等弹窗创建好之后自动填充到按钮下方
        pendingFeedback = str;
        startStatusBreathing(activity, false);
        applyPendingFeedback();
    }

    /** 把暂存的提示填到「立即验证」按钮下方的空白处 */
    private static void applyPendingFeedback() {
        if (pendingFeedback == null || feedbackView == null || dialog == null || !dialog.isShowing()) {
            return;
        }
        feedbackView.setText(pendingFeedback);
        feedbackView.setTextColor(Color.parseColor("#B33A3A"));
        feedbackView.setBackground(createRoundRectDrawable(Color.parseColor("#FDE8E8"), 10.0f));
        feedbackView.setVisibility(0);
        pendingFeedback = null;
    }

    public static void showValidationSuccess(Activity activity) {
        pendingFeedback = null;
        startStatusBreathing(activity, true);
        hideValidationFeedback();
    }

    public static void hideValidationFeedback() {
        if (feedbackView != null) {
            feedbackView.setVisibility(8);
        }
    }

    private static void startStatusBreathing(Activity activity, boolean z) {
        int color;
        int color2;
        if (statusDot == null) {
            return;
        }
        if (z) {
            color = Color.parseColor("#C6F6D5");
            color2 = Color.parseColor("#22C55E");
        } else {
            color = Color.parseColor("#FECACA");
            color2 = Color.parseColor("#EF4444");
        }
        GradientDrawable gradientDrawable = (GradientDrawable) statusDot.getBackground();
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(1);
            statusDot.setBackground(gradientDrawable);
        }
        gradientDrawable.setColor(color);
        gradientDrawable.setStroke(dp(activity, 2.0f), color2);
        statusDot.clearAnimation();
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.2f);
        alphaAnimation.setDuration(650L);
        alphaAnimation.setRepeatMode(2);
        alphaAnimation.setRepeatCount(-1);
        statusDot.startAnimation(alphaAnimation);
    }

    private static void applyClickEffect(View view) {
        if (view == null) {
            return;
        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        view2.animate().scaleX(0.94f).scaleY(0.94f).alpha(0.82f).setDuration(70L).start();
                        break;
                    case 1:
                    case 3:
                        view2.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(70L).start();
                        break;
                }
                return false;
            }
        });
    }

    private static void loadQQAvatar(final Activity activity, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://q.qlogo.cn/headimg_dl?dst_uin=" + DialogUtils.QQ_NUMBER + "&spec=640").openConnection();
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                    httpURLConnection.setInstanceFollowRedirects(true);
                    Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    httpURLConnection.disconnect();
                    if (bitmapDecodeStream != null) {
                        final Bitmap circularBitmap = DialogUtils.toCircularBitmap(bitmapDecodeStream);
                        activity.runOnUiThread(new Runnable() {
                            @Override // java.lang.Runnable
                            public void run() {
                                imageView.setImageBitmap(circularBitmap);
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public static Bitmap toCircularBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int iMin = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - iMin) / 2, (bitmap.getHeight() - iMin) / 2, iMin, iMin);
        Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(iMin, iMin, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        float f = iMin / 2.0f;
        canvas.drawCircle(f, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapCreateBitmap, 0.0f, 0.0f, paint);
        return bitmapCreateBitmap2;
    }

    public static void openBuyPage(Activity activity) {
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://baidu.com/")));
        } catch (Exception e) {
            Toast.makeText(activity, "无法打开浏览器", 0).show();
        }
    }

    public static void openQQProfile(Activity activity) {
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin=" + QQ_NUMBER + "&card_type=person&source=sharecard")));
        } catch (Exception e) {
            Toast.makeText(activity, "未检测到QQ，请先安装QQ", 0).show();
        }
    }

    public static GradientDrawable createRoundRectDrawable(int i, float f) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(i);
        gradientDrawable.setCornerRadius(f);
        return gradientDrawable;
    }

    public static GradientDrawable createGradientDrawable(GradientDrawable.Orientation orientation, int[] iArr, float f) {
        GradientDrawable gradientDrawable = new GradientDrawable(orientation, iArr);
        gradientDrawable.setCornerRadius(f);
        return gradientDrawable;
    }

    private static Bitmap decodeBase64Image(String str) {
        try {
            byte[] bArrDecode = Base64.decode(str, 0);
            return BitmapFactory.decodeByteArray(bArrDecode, 0, bArrDecode.length);
        } catch (Exception e) {
            return null;
        }
    }

    private static int dp(Context context, float f) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    private static boolean isXposedInstalled() {
        try {
            Class.forName("de.robv.android.xposed.XposedBridge");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // ==================== 插件更新弹窗 ====================

    /**
     * 显示插件更新弹窗。视觉风格复刻公告弹窗：白色圆角卡片 + 居中加粗标题 + 可滚动内容 + 底部按钮。
     *
     * @param info       来自 {@link UpdateManager.UpdateInfo} 的更新配置
     * @param onComplete 弹窗关闭后回调（可为 null）；「稍后再说」会同时记下忽略版本
     */
    public static void showUpdateDialog(final Activity activity, final UpdateManager.UpdateInfo info, final Runnable onComplete) {
        if (activity == null || activity.isFinishing()) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }
        final boolean force = info.force;

        LinearLayout container = new LinearLayout(activity);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(activity, 25.0f), dp(activity, 20.0f), dp(activity, 25.0f), dp(activity, 15.0f));
        container.setBackground(createRoundRectDrawable(-1, 20.0f));

        // 标题（居中加粗，颜色同公告弹窗）
        TextView titleView = new TextView(activity);
        titleView.setText(TextUtils.isEmpty(info.title) ? "发现新版本" : info.title);
        titleView.setTextSize(20.0f);
        titleView.setTypeface(null, 1);
        titleView.setTextColor(-13877680);
        titleView.setGravity(17);
        titleView.setPadding(0, 0, 0, dp(activity, 10.0f));
        container.addView(titleView);

        // 版本号（服务端给了才显示）
        if (!TextUtils.isEmpty(info.versionName)) {
            TextView versionView = new TextView(activity);
            versionView.setText("新版本 v" + info.versionName);
            versionView.setTextSize(13.0f);
            versionView.setTextColor(-8355712);
            versionView.setGravity(17);
            versionView.setPadding(0, 0, 0, dp(activity, 8.0f));
            container.addView(versionView);
        }

        // 更新内容（可滚动）
        TextView contentView = new TextView(activity);
        contentView.setText(TextUtils.isEmpty(info.content) ? "本次更新包含若干优化，请及时更新。" : info.content);
        contentView.setTextSize(16.0f);
        contentView.setTextColor(-13350562);
        contentView.setPadding(dp(activity, 10.0f), dp(activity, 10.0f), dp(activity, 10.0f), dp(activity, 15.0f));
        contentView.setGravity(17);
        ScrollView scrollView = new ScrollView(activity);
        scrollView.addView(contentView);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        scrollParams.setMargins(0, dp(activity, 5.0f), 0, dp(activity, 10.0f));
        container.addView(scrollView, scrollParams);

        // 强制更新提示
        if (force) {
            TextView forceView = new TextView(activity);
            forceView.setText("本次为强制更新，需更新后才能继续使用");
            forceView.setTextSize(13.0f);
            forceView.setTextColor(-18944);
            forceView.setGravity(17);
            forceView.setPadding(0, 0, 0, dp(activity, 8.0f));
            container.addView(forceView);
        }

        // 按钮区：水平排列，「立即更新」满宽；非强制时左侧多一个「稍后再说」
        LinearLayout buttonRow = new LinearLayout(activity);
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(container);

        final AlertDialog dialog = builder.create();
        // 两种模式都必须经按钮才能关闭：返回键、点空白处一律无效
        // （强制更新时连「稍后再说」都没有，只能点「立即更新」）
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final boolean[] finished = {false};
        final Runnable finish = new Runnable() {
            @Override
            public void run() {
                // 保证 onComplete 只会被触发一次（按钮回调与 onDismiss 可能都走到这里）
                if (finished[0]) {
                    return;
                }
                finished[0] = true;
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        };

        if (!force) {
            Button laterButton = createUpdateButton(activity, "稍后再说", false);
            LinearLayout.LayoutParams laterParams = new LinearLayout.LayoutParams(0, dp(activity, 44.0f), 1.0f);
            laterParams.setMargins(0, 0, dp(activity, 5.0f), 0);
            laterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateManager.markVersionSkipped(activity, info.versionCode);
                    dialog.dismiss();
                    finish.run();
                }
            });
            buttonRow.addView(laterButton, laterParams);
        }

        Button updateButton = createUpdateButton(activity, "立即更新", true);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateManager.openUpdateUrl(activity, info.url);
                if (!force) {
                    dialog.dismiss();
                    finish.run();
                }
                // 强制更新时弹窗保持显示，用户下载安装完成后重新进应用即可
            }
        });
        LinearLayout.LayoutParams updateParams = force
                ? new LinearLayout.LayoutParams(-1, dp(activity, 44.0f))
                : new LinearLayout.LayoutParams(0, dp(activity, 44.0f), 1.0f);
        buttonRow.addView(updateButton, updateParams);

        container.addView(buttonRow, new LinearLayout.LayoutParams(-1, -2));

        // 非强制模式：返回键 / ESC / 菜单键一律吞掉，必须点「稍后再说」才能关
        // 强制模式：同样吞掉，且界面里没有可关闭的按钮
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, android.view.KeyEvent event) {
                return keyCode == android.view.KeyEvent.KEYCODE_BACK
                        || keyCode == android.view.KeyEvent.KEYCODE_ESCAPE
                        || keyCode == android.view.KeyEvent.KEYCODE_MENU;
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish.run();
                if (force) {
                    // 兜底：强制更新不允许消失，被任何方式关掉都立即重新弹出
                    new Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (activity != null && !activity.isFinishing()) {
                                showUpdateDialog(activity, info, onComplete);
                            }
                        }
                    }, 200L);
                }
            }
        });

        dialog.show();

        // 圆角卡片 + 左右留边距
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(createRoundRectDrawable(-1, 20.0f));
            window.setLayout((int) (activity.getResources().getDisplayMetrics().widthPixels * 0.86f), -2);
        }
    }

    /** 更新弹窗底部按钮：统一蓝底白字；去掉 Material 默认的 elevation 阴影 */
    private static Button createUpdateButton(Context context, String text, boolean primary) {
        Button button = new Button(context);
        button.setText(text);
        button.setTextSize(16.0f);
        button.setAllCaps(false);
        // 两个按钮统一配色：蓝底 + 白字
        button.setTextColor(-1);
        button.setBackground(createRoundRectDrawable(-13330213, 22.0f));
        // 去掉按钮阴影（stateListAnimator 负责按下抬高，elevation 负责常驻阴影）
        button.setStateListAnimator(null);
        button.setElevation(0.0f);
        button.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        return button;
    }
}
