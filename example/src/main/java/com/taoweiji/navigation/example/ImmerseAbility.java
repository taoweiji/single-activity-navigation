package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;

import org.jetbrains.annotations.NotNull;

public class ImmerseAbility extends Ability {
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ability_full_screen, null);
    }

    @Override
    protected void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarBackgroundColor(Color.TRANSPARENT);
        setTitle("沉浸模式");
        setContentViewMarginTop(0);
    }
}
