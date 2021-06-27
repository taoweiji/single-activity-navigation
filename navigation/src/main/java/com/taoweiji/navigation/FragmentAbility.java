package com.taoweiji.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class FragmentAbility extends Ability {

    private final Fragment fragment;

    public FragmentAbility(Fragment fragment) {
        this.fragment = fragment;
        NavController.addFragmentAbility(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getContext());
        FragmentActivity activity = (FragmentActivity) getContext();
        layout.setId(this.hashCode());
        activity.getSupportFragmentManager().beginTransaction().add(layout.getId(), fragment, null).commit();
        return layout;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
