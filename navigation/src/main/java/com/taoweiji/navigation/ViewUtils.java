package com.taoweiji.navigation;

import android.content.Context;

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
}
