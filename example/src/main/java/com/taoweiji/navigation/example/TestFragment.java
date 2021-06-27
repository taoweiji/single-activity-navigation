package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.NavController;

import org.jetbrains.annotations.NotNull;

public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ability_user, null);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
        Ability ability = NavController.findAbility(this);
        ability.createDefaultToolbar();
        ability.setTitle("直接跳转 Fragment");
        TextView info = view.findViewById(R.id.info);
        info.setText("TestFragment");
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ability ability = NavController.findAbility(TestFragment.this);
                NavController nav = NavController.findNavController(TestFragment.this);
                ability.setStatusBarColor(Color.RED);
                nav.navigate("weather");
            }
        });
    }

}
