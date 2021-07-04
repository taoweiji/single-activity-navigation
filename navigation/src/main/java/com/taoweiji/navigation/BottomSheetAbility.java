package com.taoweiji.navigation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public abstract class BottomSheetAbility extends Ability {

    private boolean canceledOnTouchOutside = true;
    private int outsideBackgroundColor = Color.parseColor("#80000000");

    public void show(NavController navController) {
        navController.navigate(Destination.with(this), NavOptions.NONE);
        getToolbar().setVisibility(View.GONE);
        ValueAnimator objectAnimator = ObjectAnimator.ofFloat(0, 1);
        objectAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            int alpha = Color.alpha(outsideBackgroundColor);
            int red = Color.red(outsideBackgroundColor);
            int green = Color.green(outsideBackgroundColor);
            int blue = Color.blue(outsideBackgroundColor);
            setBackgroundColor(Color.argb((int) (alpha * value), red, green, blue));
        });
        objectAnimator.setDuration(400);
        objectAnimator.start();
        Animation animation = AnimationUtils.loadAnimation(getContext(), displayAnimation());
        getDecorView().getContentView().startAnimation(animation);
        if (canceledOnTouchOutside) {
            getDecorView().setOnClickListener(v -> dismiss());
        }
        this.setCanceledOnTouchOutside(true);
    }

    protected int displayAnimation() {
        return R.anim.bottom_sheet_slide_in_up;
    }

    protected int dismissAnimation() {
        return R.anim.bottom_sheet_slide_out_down;
    }

    public void setOutsideBackgroundColor(int outsideBackgroundColor) {
        this.outsideBackgroundColor = outsideBackgroundColor;
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public void dismiss() {
        ValueAnimator objectAnimator = ObjectAnimator.ofFloat(1, 0);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                int alpha = Color.alpha(outsideBackgroundColor);
                int red = Color.red(outsideBackgroundColor);
                int green = Color.green(outsideBackgroundColor);
                int blue = Color.blue(outsideBackgroundColor);
                setBackgroundColor(Color.argb((int) (alpha * value), red, green, blue));
            }
        });
        objectAnimator.setDuration(400);
        objectAnimator.start();
        Animation animation = AnimationUtils.loadAnimation(getContext(), dismissAnimation());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getDecorView().getContentView().startAnimation(animation);
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }

}
