package com.taoweiji.navigation;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class AbilityViewParent extends LinearLayout {
    NavController navController;
    private final Ability ability;

    public AbilityViewParent(@NonNull Context context, @Nullable NavController navController, @NonNull Ability ability) {
        super(context);
        this.navController = navController;
        this.ability = ability;
        setOrientation(LinearLayout.VERTICAL);
        setClickable(true);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 如果不是由 NavController 管理，那么就由 View 自己实现管理
        if (navController == null) {
            ability.performOnResume();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 如果不是由 NavController 管理，那么就由 View 自己实现管理
        if (navController == null) {
            ability.performOnPause();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public NavController getNavController() {
        return navController;
    }

    public Ability getAbility() {
        return ability;
    }
}