package com.taoweiji.navigation;

import android.app.Activity;
import android.content.Context;
import android.view.WindowMetrics;

public class ViewUtils {

    public static int dp2px(Context context, Number dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp.floatValue());
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    public static int getScreenWidth(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
    }

}
