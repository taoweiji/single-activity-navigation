package com.taoweiji.navigation;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

class AbilityViewParent extends LinearLayout {
    private final NavController navController;
    private final Ability ability;

    public AbilityViewParent(@NonNull Context context, NavController navController, Ability ability) {
        super(context);
        this.navController = navController;
        this.ability = ability;
        setOrientation(LinearLayout.VERTICAL);
        setClickable(true);
        setBackgroundColor(Color.WHITE);
    }

    public NavController getNavController() {
        return navController;
    }

    public Ability getAbility() {
        return ability;
    }
}