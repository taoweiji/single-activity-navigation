package com.taoweiji.navigation.example;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.StatusBarHelper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LandscapeAbility extends Ability {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ability_player, null);
    }

    Runnable task;

    @Override
    protected void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置标题栏为透明
        getToolbar().setBackgroundColor(Color.TRANSPARENT);
        // 隐藏状态栏
        getToolbar().setVisibility(View.GONE);
        // 让页面内容置顶
        setContentViewMarginTop(0);
        // 设置全屏
        setStatusBarStyle(StatusBarHelper.STYLE_FULLSCREEN);
        SurfaceView surfaceView = findViewById(R.id.surface_view);
        Handler uiHandler = new Handler(Looper.getMainLooper());

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToolbar().setVisibility(View.VISIBLE);
                if (task != null) {
                    uiHandler.removeCallbacks(task);
                }
                task = () -> {
                    task = null;
                    getToolbar().setVisibility(View.GONE);
                };
                // 5秒隐藏返回按钮
                uiHandler.postDelayed(task, 5000);
            }
        });

        mediaPlayer = new MediaPlayer();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(@NonNull SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mediaPlayer.setDataSource(getContext().getAssets().openFd("video_demo2.mp4"));
                    }
                    mediaPlayer.prepare();
                    mediaPlayer.setDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
    }


    @Override
    public void finish() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.finish();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
