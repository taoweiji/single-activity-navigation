package com.taoweiji.navigation.example;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.ViewUtils;

import org.jetbrains.annotations.NotNull;

public class ImageAbility extends Ability {
    private int leftMargin;
    private int topMargin;
    private int width;
    private int height;

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setContentViewMarginTop(0);
        setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        ImageView imageView = new ImageView(getContext());
        this.leftMargin = getArguments().getInt("x");
        this.topMargin = getArguments().getInt("y");
        this.width = getArguments().getInt("width");
        this.height = getArguments().getInt("height");
        String url = getArguments().getString("url");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        lp.leftMargin = leftMargin;
        lp.topMargin = topMargin;
        relativeLayout.addView(imageView, lp);
        Glide.with(getContext()).load(url).into(imageView);
        int screenWidth = ViewUtils.getScreenWidth(getContext());
        int targetTopMargin = ViewUtils.dp2px(getContext(), 56) + ViewUtils.getStatusBarHeight(getContext());
        ValueAnimator objectAnimator = ObjectAnimator.ofFloat(0, 1);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                lp.leftMargin = (int) (leftMargin * (1 - ratio));
                lp.topMargin = (int) (topMargin + (targetTopMargin - topMargin) * ratio);
                lp.width = (int) (width + (screenWidth - width) * ratio);
                lp.height = (int) (height / (float) width * lp.width);
                imageView.setLayoutParams(lp);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setBackgroundColor(Color.argb(ratio, 0f, 0f, 0f));
                }
            }
        });
        objectAnimator.setDuration(300);
        objectAnimator.start();
        return relativeLayout;
    }

    @Override
    public void finish() {
        super.finish();

    }
}
