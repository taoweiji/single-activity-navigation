package com.taoweiji.navigation;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AbilityRouteBuilder {
    Ability builder(Context context);

    @LaunchMode
    default int launchMode() {
        return Intent.FLAG_ACTIVITY_NEW_TASK;
    }

    @IntDef({Intent.FLAG_ACTIVITY_NEW_TASK,
            Intent.FLAG_ACTIVITY_CLEAR_TOP,
            Intent.FLAG_ACTIVITY_SINGLE_TOP,
            Intent.FLAG_ACTIVITY_CLEAR_TASK})
    @Retention(RetentionPolicy.SOURCE)
    @interface LaunchMode {

    }
}
