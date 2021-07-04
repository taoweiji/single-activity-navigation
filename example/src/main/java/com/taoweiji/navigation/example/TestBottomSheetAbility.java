package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.BottomSheetAbility;
import com.taoweiji.navigation.ViewUtils;

import org.jetbrains.annotations.NotNull;

public class TestBottomSheetAbility extends BottomSheetAbility {

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        RelativeLayout linearLayout = new RelativeLayout(getContext());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        int dp20 = ViewUtils.dp2px(getContext(), 20);
        drawable.setCornerRadii(new float[]{dp20, dp20, dp20, dp20, 0, 0, 0, 0});
        RelativeLayout view = new RelativeLayout(getContext());
        view.setBackground(drawable);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewUtils.dp2px(getContext(), 500));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearLayout.addView(view, lp);

        ImageView imageView = new ImageView(getContext());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imageView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        int dp10 = ViewUtils.dp2px(getContext(), 10);
        imageView.setPadding(dp10, dp10, dp10, dp10);
        RelativeLayout.LayoutParams closeLP = new RelativeLayout.LayoutParams(ViewUtils.dp2px(getContext(), 50), ViewUtils.dp2px(getContext(), 50));
        closeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        view.addView(imageView, closeLP);
        return linearLayout;
    }
}
