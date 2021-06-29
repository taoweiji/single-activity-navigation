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
import com.taoweiji.navigation.AbilityResultContracts;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.ViewUtils;
import com.taoweiji.navigation.example.result.TestResultAbility;

import org.jetbrains.annotations.NotNull;

public class PopAndPushAbility extends Ability {
    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        createDefaultToolbar();
        setTitle("跳转页面，且关闭当前页面");
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        Button button = new Button(getContext());
        layout.addView(button);
        button.setText("跳转页面");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().navigate(new TestResultAbility()).registerListener(new AbilityResultContracts.Listener() {
                    @Override
                    public void onNavigateEnd() {
                        finish();
                    }
                });
            }
        });
        return layout;
    }
}
