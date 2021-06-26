package com.taoweiji.navigation.example.mvvm;

import android.graphics.Color;
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
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        info = new TextView(getContext());
        button = new Button(getContext());
        info.setGravity(Gravity.CENTER);
        button.setText("更新");
        linearLayout.addView(info);
        linearLayout.addView(button);
        setBackgroundColor(Color.WHITE);
        return linearLayout;
    }

    @Override
    protected void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherViewModel.weatherData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer temperature) {
                info.setText("当前温度：" + temperature);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherViewModel.updateWeather();
            }
        });


//        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            String url = result.getData().getStringExtra("url");
//        }).launch(new Intent(getContext(), QrcodeActivity.class));
    }
}
