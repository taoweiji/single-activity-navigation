package com.taoweiji.navigation.example;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.ViewUtils;

import org.jetbrains.annotations.NotNull;

public class TestOverViewAbility extends Ability {

    private BottomSheetView sheetView;

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        Button button = new Button(getContext());
        button.setText("显示覆盖层");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetView = new BottomSheetView(getContext());
                getCoverView().addView(sheetView);
                sheetView.show();
            }
        });
        linearLayout.addView(button);

        Button button2 = new Button(getContext());
        button2.setText("显示 BottomSheetAbility");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestBottomSheetAbility().show(findNavController());
            }
        });
        linearLayout.addView(button2);
        return linearLayout;
    }

    @Override
    public void onBackPressed() {
        if (sheetView != null && sheetView.isShow()) {
            sheetView.dismiss();
            return;
        }
        super.onBackPressed();
    }

    static class BottomSheetView extends RelativeLayout {

        private RelativeLayout container;
        private boolean show;

        public BottomSheetView(Context context) {
            super(context);
            initView();
            setOnClickListener(v -> dismiss());
        }

        public boolean isShow() {
            return show;
        }

        public void show() {
            ValueAnimator objectAnimator = ObjectAnimator.ofFloat(0, 1);
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setBackgroundColor(Color.argb(0.5f * value, 0, 0, 0));
                    }
                }
            });
            objectAnimator.start();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_up);
            container.startAnimation(animation);
            show = true;
        }

        public void dismiss() {
            show = false;
            ValueAnimator objectAnimator = ObjectAnimator.ofFloat(1, 0);
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setBackgroundColor(Color.argb(0.5f * value, 0, 0, 0));
                    }
                }
            });
            objectAnimator.start();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (getParent() != null) {
                        ((ViewGroup) getParent()).removeView(BottomSheetView.this);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            container.startAnimation(animation);
        }

        private void initView() {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.WHITE);
            int dp20 = ViewUtils.dp2px(getContext(), 20);
            drawable.setCornerRadii(new float[]{dp20, dp20, dp20, dp20, 0, 0, 0, 0});
            this.container = new RelativeLayout(getContext());
            container.setBackground(drawable);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewUtils.dp2px(getContext(), 500));
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(container, lp);

            ImageView imageView = new ImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            imageView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            int dp10 = ViewUtils.dp2px(getContext(), 10);
            imageView.setPadding(dp10, dp10, dp10, dp10);
            LayoutParams closeLP = new LayoutParams(ViewUtils.dp2px(getContext(), 50), ViewUtils.dp2px(getContext(), 50));
            closeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            container.addView(imageView, closeLP);
        }
    }
}
