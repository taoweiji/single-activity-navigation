package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;

import org.jetbrains.annotations.NotNull;

public class UiAbility extends Ability {
    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        createDefaultToolbar();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button setStatusBarTextStyle = new Button(getContext());
        setStatusBarTextStyle.setText("设置状态栏风格");
        setStatusBarTextStyle.setOnClickListener(v -> setStatusBarTextStyle(StatusBarTextStyle.BLACK));

        Button settingToolbarColor = new Button(getContext());
        settingToolbarColor.setText("设置标题栏颜色");
        settingToolbarColor.setOnClickListener(v -> setToolbarBackgroundColor(Color.RED));
        Button settingBackgroundColor = new Button(getContext());
        settingBackgroundColor.setText("设置背景颜色");
        settingBackgroundColor.setOnClickListener(v -> setBackgroundColor(Color.RED));
        Button settingStatusBarColor = new Button(getContext());
        settingStatusBarColor.setText("设置状态栏颜色");
        settingStatusBarColor.setOnClickListener(v -> setStatusBarColor(Color.RED));
        linearLayout.addView(setStatusBarTextStyle);
        linearLayout.addView(settingStatusBarColor);
        linearLayout.addView(settingToolbarColor);
        linearLayout.addView(settingBackgroundColor);
        return linearLayout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatusBarTextStyle(StatusBarTextStyle.WHITE);
    }
}
