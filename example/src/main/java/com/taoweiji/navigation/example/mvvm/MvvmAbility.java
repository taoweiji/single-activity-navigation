package com.taoweiji.navigation.example.mvvm;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;

import org.jetbrains.annotations.NotNull;

public class MvvmAbility extends Ability {
    WeatherViewModel weatherViewModel = new WeatherViewModel();
    private TextView info;
    private Button button;

    public MvvmAbility() {

    }

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        createDefaultToolbar();
        setTitle("Lifecycle、LiveData 实现 MVVM");
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        info = new TextView(getContext());
        button = new Button(getContext());
        info.setGravity(Gravity.CENTER);
        button.setText("更新");
        linearLayout.addView(info, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(button, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return linearLayout;
    }

    @Override
    protected void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherViewModel.weatherData.observe(this, temperature -> info.setText("当前温度：" + temperature));
        button.setOnClickListener(v -> weatherViewModel.updateWeather());
        weatherViewModel.updateWeather();
    }
}
