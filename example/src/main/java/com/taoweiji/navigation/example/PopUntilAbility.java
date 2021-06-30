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

import org.jetbrains.annotations.NotNull;

public class PopUntilAbility extends Ability {
    @NonNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setTitle("有条件关闭页面");
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        Button button = new Button(getContext());
        layout.addView(button);
        button.setText("跳转 SecondAbility");
        button.setOnClickListener(v -> findNavController().navigate(new SecondAbility()));
        return layout;
    }

    public static class SecondAbility extends Ability {
        @NonNull
        @Override
        protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            setTitle("SecondAbility");
            RelativeLayout layout = new RelativeLayout(getContext());
            layout.setGravity(Gravity.CENTER);
            Button button = new Button(getContext());
            layout.addView(button);
            button.setText("popUntil");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findNavController().popUntil(ability -> ability instanceof IndexAbility);
                }
            });
            return layout;
        }
    }

}
