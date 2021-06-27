package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.BundleBuilder;

import org.jetbrains.annotations.NotNull;

public class TestResultAbility extends Ability {
    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        Button button = new Button(getContext());
        button.setText("跳转页面");
        layout.addView(button);
        return layout;
    }

    @Override
    protected void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setResult(new BundleBuilder().put("msg", "TestResultAbility").build());
    }
}
