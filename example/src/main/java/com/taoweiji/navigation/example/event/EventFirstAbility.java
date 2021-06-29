package com.taoweiji.navigation.example.event;

import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;

public class EventFirstAbility extends Ability {
    public static final int FINISH_EVENT_FIRST_ABILITY = 1009;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        TextView hello = new TextView(getContext());
        hello.setGravity(Gravity.CENTER);
        hello.setText("跳转到 EventSecondAbility");
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController().navigate(new EventSecondAbility());
            }
        });
        return hello;
    }

    @Override
    protected void onEvent(Message message) {
        if (message.what == FINISH_EVENT_FIRST_ABILITY) {
            Toast.makeText(getContext(), "收到通知关闭页面", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}
