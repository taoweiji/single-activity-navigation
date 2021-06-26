package com.taoweiji.navigation;

import android.content.Context;

public class ViewUtils {

    public static int dp2px(Context context, Number dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp.floatValue());
    }
}
