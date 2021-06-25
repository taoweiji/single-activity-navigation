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

class AbilityFragmentContainer extends AbilityContainer {

    private final Fragment fragment;

    public AbilityFragmentContainer(Fragment fragment) {
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getContext());
        FragmentActivity activity = (FragmentActivity) getContext();
        int id = this.hashCode();
        if (container != null) {
            container.setId(id);
            activity.getSupportFragmentManager().beginTransaction().add(id, fragment, null).commit();
        }
        return layout;
    }
}
