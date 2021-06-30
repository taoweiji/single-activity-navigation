package com.taoweiji.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

class AbilityViewParent extends RelativeLayout {
    NavController navController;
    private final Ability ability;
    private Toolbar toolbar;
    private View content;
    private int contentViewMarginTop = -1;

    public AbilityViewParent(@NonNull Context context, @Nullable NavController navController, @NonNull Ability ability) {
        super(context);
        this.navController = navController;
        this.ability = ability;
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

    public void addContent(View content) {
        this.content = content;
        updateView();
        this.addView(content, 0);
    }

    void addToolbar(Toolbar toolbar) {
        toolbar.setId(toolbar.hashCode());
        this.toolbar = toolbar;
        this.addView(toolbar, getChildCount());
        updateView();
    }

    public void setContentViewMarginTop(int marginTop) {
        this.contentViewMarginTop = marginTop;
        if (content == null) return;
        updateView();
    }

    public void updateView() {
        if (content == null) return;
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (contentViewMarginTop >= 0) {
            lp.topMargin = contentViewMarginTop;
        } else if (toolbar != null) {
            lp.addRule(RelativeLayout.BELOW, toolbar.getId());
        }
        content.setLayoutParams(lp);
    }

    Toolbar getToolbar() {
        return toolbar;
    }
}