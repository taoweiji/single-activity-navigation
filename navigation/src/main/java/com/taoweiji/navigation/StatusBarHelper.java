package com.taoweiji.navigation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatusBarHelper {
    /**
     * 白色的文字和图标
     */
    public static final int STYLE_WHITE = 0;
    /**
     * 黑色的文字和图标
     */
    public static final int STYLE_BLACK = 1;
    /**
     * 全屏，包含刘海区域
     */
    public static final int STYLE_FULLSCREEN = 2;
    /**
     * 全屏，不包含刘海区域
     */
    public static final int STYLE_FULLSCREEN_WITHOUT_CUTOUT = 3;

    @IntDef({STYLE_WHITE, STYLE_BLACK, STYLE_FULLSCREEN, STYLE_FULLSCREEN_WITHOUT_CUTOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }

    /**
     * 修改状态栏的文字和图标颜色，STYLE_WHITE, StatusBarHelper.STYLE_BLACK,StatusBarHelper.STYLE_HIDE
     */
    public static void setTextStyle(Activity activity, @Style int style) {
        Window window = activity.getWindow();
        if (style == STYLE_WHITE) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            WindowManager.LayoutParams attrs = window.getAttributes();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            }
            window.setAttributes(attrs);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
        } else if (style == STYLE_FULLSCREEN) {
            window.setStatusBarColor(Color.TRANSPARENT);
            WindowManager.LayoutParams attrs = window.getAttributes();
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
            }
            window.setAttributes(attrs);
        } else if (style == STYLE_BLACK) {
            WindowManager.LayoutParams attrs = window.getAttributes();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            }
            window.setAttributes(attrs);
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.setStatusBarColor(Color.BLACK);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        } else if (style == STYLE_FULLSCREEN_WITHOUT_CUTOUT) {
            window.setStatusBarColor(Color.TRANSPARENT);
            WindowManager.LayoutParams attrs = window.getAttributes();
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
            window.setAttributes(attrs);
        }
    }

    /**
     * 开启沉浸模式
     */
    public static void openImmerseStyle(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
