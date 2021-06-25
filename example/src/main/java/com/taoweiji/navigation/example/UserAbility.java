package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.AbilityContainer;
import com.taoweiji.navigation.NavController;

import org.jetbrains.annotations.NotNull;

public class UserAbility extends AbilityContainer {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText("user id = " + getArguments().getString("id"));
        return inflater.inflate(R.layout.ability_user, null);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
        TextView info = view.findViewById(R.id.info);
        int id = getArguments().getInt("id");
        info.setText(String.valueOf(id));

        view.findViewById(R.id.back).setOnClickListener(v -> {
            NavController nav = NavController.findNavController(view);
            nav.pop();
        });
        view.findViewById(R.id.next).setOnClickListener(v -> {
            NavController nav = NavController.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putInt("id", id + 1);
            nav.navigate(new UserAbility(), bundle);
        });
    }
}