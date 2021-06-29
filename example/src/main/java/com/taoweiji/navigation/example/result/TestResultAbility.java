package com.taoweiji.navigation.example.result;

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
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        createDefaultToolbar().setTitle("设置返回值 msg = TestResultAbility");
        return new View(getContext());
    }

    @Override
    protected void onViewCreated(@NonNull @NotNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setResult(new BundleBuilder().put("msg", "TestResultAbility").build());
    }
}
