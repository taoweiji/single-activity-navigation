package com.taoweiji.navigation.example.dialog;

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

public class DialogAbility extends Ability {
    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        setTitle("DialogAbility");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        Button button1 = new Button(getContext());
        button1.setText("普通对话框（不推荐）");
        layout.addView(button1);
        Button button2 = new Button(getContext());
        button2.setText("Fragment 对话框（推荐）");
        layout.addView(button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialogFragment = new MyDialog(DialogAbility.this);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "hello");
            }
        });
        return layout;
    }
}

