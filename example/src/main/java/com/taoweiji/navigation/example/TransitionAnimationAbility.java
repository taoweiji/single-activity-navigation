package com.taoweiji.navigation.example;

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
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavOptions;

import org.jetbrains.annotations.NotNull;

public class TransitionAnimationAbility extends Ability {
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        Button leftRight = new Button(getContext());
        leftRight.setText("左右");
        leftRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().navigate(Destination.with(new SimpleAbility()).withNavOptions(NavOptions.LEFT_RIGHT));
            }
        });
        linearLayout.addView(leftRight);

        Button activityDefault = new Button(getContext());
        activityDefault.setText("Android 默认");
        activityDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().navigate(Destination.with(new SimpleAbility()).withNavOptions(NavOptions.DEFAULT));
            }
        });
        linearLayout.addView(activityDefault);

        Button upDown = new Button(getContext());
        upDown.setText("上下");
        upDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().navigate(Destination.with(new SimpleAbility()).withNavOptions(NavOptions.UP_DOWN));
            }
        });
        linearLayout.addView(upDown);


        Button none = new Button(getContext());
        none.setText("无动画");
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().navigate(Destination.with(new SimpleAbility()).withNavOptions(NavOptions.NONE));
            }
        });
        linearLayout.addView(none);

        Button overridePendingTransition = new Button(getContext());
        overridePendingTransition.setText("目标页面覆盖动画");
        overridePendingTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().navigate(new OverridePendingTransitionAbility());
            }
        });

        linearLayout.addView(overridePendingTransition);
        return linearLayout;
    }

    static class OverridePendingTransitionAbility extends Ability {

        @Override
        protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            setTitle("overridePendingTransition");
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            return new View(getContext());
        }
    }
}
