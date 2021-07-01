package com.taoweiji.navigation;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

class ToolbarAndStatusBarWrapper {
    private Boolean statusBarTextStyle;
    /**
     * 自动创建返回键
     */
    private boolean defaultDisplayHomeAsUpEnabled = true;
    private Toolbar toolbar;
    private final Ability ability;

    public ToolbarAndStatusBarWrapper(Ability ability) {
        this.ability = ability;
    }

    private void setStatusBarTextStyleInner(Boolean white) {
        this.statusBarTextStyle = white;
        // 默认开启沉浸模式，交给Ability自己实现状态栏颜色改变，
        Window window = getActivity().getWindow();
        if (!white) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    private Activity getActivity() {
        return ability.getActivity();
    }

    public void setStatusBarTextStyle(boolean white) {
        this.statusBarTextStyle = white;
        setStatusBarTextStyleInner(white);
    }

    public void setDefaultDisplayHomeAsUpEnabled(boolean defaultDisplayHomeAsUpEnabled) {
        this.defaultDisplayHomeAsUpEnabled = defaultDisplayHomeAsUpEnabled;
        if (toolbar == null) return;
        if (!defaultDisplayHomeAsUpEnabled) {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        } else {
            updateDefaultDisplayHome();
        }
    }

    public boolean isDefaultDisplayHomeAsUpEnabled() {
        return defaultDisplayHomeAsUpEnabled;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    private boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    public void setTitle(CharSequence title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    void onResume() {
        if (statusBarTextStyle != null) {
            setStatusBarTextStyleInner(statusBarTextStyle);
        }
    }

    public void setToolbarBackgroundColor(int color) {
        setStatusBarTextStyleInner(!isLightColor(color));
        if (toolbar == null) return;
        boolean textColorLight = !isLightColor(color);
        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(textColorLight ? Color.WHITE : Color.BLACK);
        toolbar.setSubtitleTextColor(textColorLight ? Color.WHITE : Color.BLACK);
        updateDefaultDisplayHome();
    }

    private void updateDefaultDisplayHome() {
        if (toolbar == null) return;
        boolean isLight = false;
        if (statusBarTextStyle != null) {
            isLight = statusBarTextStyle;
        }
        if (isDefaultDisplayHomeAsUpEnabled()) {
            boolean showBack = false;
            NavController nav = ability.findNavController();
            if (nav != null && (nav.getStackCount() > 0 && !nav.isRootAbility(ability))) {
                showBack = true;
            }
            if (showBack) {
                toolbar.setNavigationIcon(isLight ? R.drawable.ic_ab_back_material_dark : R.drawable.ic_ab_back_material_light);
            } else {
                toolbar.setNavigationIcon(null);
            }
            toolbar.setNavigationOnClickListener(v -> ability.onBackPressed());
        }
    }
}
