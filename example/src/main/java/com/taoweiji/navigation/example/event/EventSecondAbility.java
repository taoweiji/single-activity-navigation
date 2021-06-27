package com.taoweiji.navigation.example.event;

import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.NavController;

public class EventSecondAbility extends Ability {
    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        TextView hello = new TextView(getContext());
        hello.setGravity(Gravity.CENTER);
        hello.setText("发送关闭EventFirstAbility通知");
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = EventFirstAbility.FINISH_EVENT_FIRST_ABILITY;
                sendAbilityEvent(message);
            }
        });
        return hello;
    }
}
