package com.taoweiji.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class AbilityViewParent extends FrameLayout {
    NavController navController;
    private final Ability ability;
    private Toolbar toolbar;
    private View content;
    private int contentViewMarginTop = -1;
    private final RelativeLayout contentLayout;
    private final FrameLayout coverLayout;

    public AbilityViewParent(@NonNull Context context, @Nullable NavController navController, @NonNull Ability ability) {
        super(context);
        this.navController = navController;
        this.ability = ability;
        setClickable(true);
        setBackgroundColor(Color.WHITE);
        contentLayout = new RelativeLayout(getContext());
        coverLayout = new FrameLayout(getContext()) {
            @Override
            protected LayoutParams generateDefaultLayoutParams() {
                return super.generateDefaultLayoutParams();
            }
        };
        addView(contentLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(coverLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    NavController getNavController() {
        return navController;
    }

    Ability getAbility() {
        return ability;
    }

    void addContent(View content) {
        this.content = content;
        updateView();
        contentLayout.addView(content, 0);
    }

    void addToolbar(Toolbar toolbar) {
        toolbar.setId(toolbar.hashCode());
        this.toolbar = toolbar;
        contentLayout.addView(toolbar);
        updateView();
    }

    void setContentViewMarginTop(int marginTop) {
        this.contentViewMarginTop = marginTop;
        if (content == null) return;
        updateView();
    }

    private void updateView() {
        if (content == null) return;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (contentViewMarginTop >= 0) {
            lp.topMargin = contentViewMarginTop;
        } else if (toolbar != null) {
            lp.addRule(RelativeLayout.BELOW, toolbar.getId());
        }
        content.setLayoutParams(lp);
    }

    public View getContentView() {
        return content;
    }

    public FrameLayout getCoverView() {
        return coverLayout;
    }

    Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * {@link #getCoverView()}.
     */
    @Deprecated
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    /**
     * {@link #getCoverView()}.
     */
    @Deprecated
    @Override
    public void addView(View child) {
        super.addView(child);
    }

    /**
     * {@link #getCoverView()}.
     */
    @Deprecated
    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }

    /**
     * {@link #getCoverView()}.
     */
    @Deprecated
    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
    }

    /**
     * {@link #getCoverView()}.
     */
    @Deprecated
    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

}