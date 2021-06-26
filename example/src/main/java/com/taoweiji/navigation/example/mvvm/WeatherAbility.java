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
import androidx.lifecycle.Observer;

import com.taoweiji.navigation.Ability;

import org.jetbrains.annotations.NotNull;

public class WeatherAbility extends Ability {
    WeatherViewModel weatherViewModel = new WeatherViewModel();
    private TextView info;
    private Button button;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        info = new TextView(getContext());
        button = new Button(getContext());
        info.setGravity(Gravity.CENTER);
        button.setText("更新");
        linearLayout.addView(info);
        linearLayout.addView(button);
        return linearLayout;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherViewModel.weatherData.observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                info.setText("当前温度：" + weather.temperature);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherViewModel.updateWeather();
            }
        });
    }
}
