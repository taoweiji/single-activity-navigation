package com.taoweiji.navigation;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;

import androidx.appcompat.widget.Toolbar;

class ToolbarAndStatusBarWrapper {
    private Ability.StatusBarTextStyle statusBarTextStyle;
    /**
     * 自动创建返回键
     */
    private boolean defaultDisplayHomeAsUpEnabled = true;
    private Toolbar toolbar;
    private final Ability ability;

    public ToolbarAndStatusBarWrapper(Ability ability) {
        this.ability = ability;
    }

    private void setStatusBarTextStyleInner(Ability.StatusBarTextStyle style) {
        this.statusBarTextStyle = style;
        if (style == Ability.StatusBarTextStyle.WHITE) {
            StatusBarHelper.setTextStyle(ability.getActivity(), StatusBarHelper.STYLE_WHITE);
        } else if (style == Ability.StatusBarTextStyle.BLACK) {
            StatusBarHelper.setTextStyle(ability.getActivity(), StatusBarHelper.STYLE_BLACK);
        } else if (style == Ability.StatusBarTextStyle.HIDE) {
            StatusBarHelper.setTextStyle(ability.getActivity(), StatusBarHelper.STYLE_HIDE);
        }
    }

    public void setStatusBarTextStyle(Ability.StatusBarTextStyle white) {
        this.statusBarTextStyle = white;
        setStatusBarTextStyleInner(white);
    }

    public void setDefaultDisplayHomeAsUpEnabled(boolean defaultDisplayHomeAsUpEnabled) {
        this.defaultDisplayHomeAsUpEnabled = defaultDisplayHomeAsUpEnabled;
        updateNavigationIcon();
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(ability::onOptionsItemSelected);
            updateNavigationIcon();
        }
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
        boolean textColorLight = !isLightColor(color);
        if (textColorLight) {
            setStatusBarTextStyleInner(Ability.StatusBarTextStyle.WHITE);
        } else {
            setStatusBarTextStyleInner(Ability.StatusBarTextStyle.BLACK);
        }
        if (toolbar == null) return;
        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(textColorLight ? Color.WHITE : Color.BLACK);
        toolbar.setSubtitleTextColor(textColorLight ? Color.WHITE : Color.BLACK);
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(textColorLight ? Color.WHITE : Color.BLACK, PorterDuff.Mode.SRC_IN);
        }
    }

    private void updateNavigationIcon() {
        if (toolbar == null) return;
        if (!defaultDisplayHomeAsUpEnabled) {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        } else {
            boolean showBack = false;
            NavController nav = ability.findNavController();
            if (nav != null && (nav.getStackCount() > 0 && !nav.isRootAbility(ability))) {
                showBack = true;
            }
            if (showBack) {
                toolbar.setNavigationIcon(R.drawable.ic_ab_back_material_light);
            } else {
                toolbar.setNavigationIcon(null);
            }
            toolbar.setNavigationOnClickListener(v -> ability.onBackPressed());
        }
    }
}
