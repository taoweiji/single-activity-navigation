package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.SoftKeyboardHelper;
import com.taoweiji.navigation.ViewUtils;

import org.jetbrains.annotations.NotNull;

public class SoftKeyboardAbility extends Ability {

    private EditText editText;

    @Nullable
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.addView(getContent(), RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        EditText floatingEditText = new EditText(getContext());
        floatingEditText.setBackgroundColor(Color.GRAY);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.addView(floatingEditText, lp);
        return relativeLayout;
    }

    private View getContent() {
        LinearLayout content = new LinearLayout(getContext());
        content.setPadding(0, ViewUtils.dp2px(getContext(), 200), 0, 0);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        this.editText = new EditText(getContext());
        editText.setBackgroundColor(Color.GRAY);
        content.addView(editText);
        return content;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SoftKeyboardHelper.showSoftKeyboard(editText);
    }
}