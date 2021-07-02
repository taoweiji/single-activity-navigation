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
import com.taoweiji.navigation.ViewUtils;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class UiAbility extends Ability {
    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button setStatusBarTextStyle = new Button(getContext());
        setStatusBarTextStyle.setText("设置状态栏风格");
        AtomicInteger flag = new AtomicInteger();
        setStatusBarTextStyle.setOnClickListener(v -> {
//            setStatusBarTextStyle((flag.getAndIncrement()) % 2 == 0);
            setStatusBarTextStyle(StatusBarTextStyle.HIDE);
        });

        Button settingToolbarColor = new Button(getContext());
        settingToolbarColor.setText("设置标题栏颜色");
        settingToolbarColor.setOnClickListener(v -> setToolbarBackgroundColor(Color.RED));

        Button settingBackgroundColor = new Button(getContext());
        settingBackgroundColor.setText("设置背景颜色");
        settingBackgroundColor.setOnClickListener(v -> setBackgroundColor(Color.RED));

        Button setToolbarHeight = new Button(getContext());
        setToolbarHeight.setText("设置Toolbar高度");
        setToolbarHeight.setOnClickListener(v -> setToolbarHeight(ViewUtils.dp2px(getContext(), 40)));

        linearLayout.addView(setStatusBarTextStyle);
        linearLayout.addView(settingToolbarColor);
        linearLayout.addView(settingBackgroundColor);
        linearLayout.addView(setToolbarHeight);
        return linearLayout;
    }
}
