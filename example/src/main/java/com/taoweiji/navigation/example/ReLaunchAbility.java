package com.taoweiji.navigation.example;

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
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.example.result.TestResultAbility;

import org.jetbrains.annotations.NotNull;

public class ReLaunchAbility extends Ability {
    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setTitle("跳转页面，且关闭所有页面");
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        Button button = new Button(getContext());
        layout.addView(button);
        button.setText("跳转页面");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().relaunch(Destination.with(new TestResultAbility()));
            }
        });
        return layout;
    }
}
