package com.taoweiji.navigation.example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.ViewUtils;

import org.jetbrains.annotations.NotNull;

public class GlobalLifecycleAbility extends Ability {
    private TextView logTextView;

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        this.logTextView = new TextView(getContext());
        int dp20 = ViewUtils.dp2px(getContext(), 20);
        this.logTextView.setPadding(dp20, dp20, dp20, dp20);
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(logTextView);
        return scrollView;
    }

    @Override
    protected void onViewCreated(View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logTextView.setText(MyApplication.logs.toString());
    }
}
