package com.taoweiji.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

/**
 * Slice 是页面的最小单位，提供完整的生命周期，是仿照Fragment的实现
 */
public interface Ability extends LifecycleOwner {
    Context getContext();

    Bundle getArguments();


    void setArguments(Bundle arguments);

    void onStart();

    void onStop();

    void onResume();

    void onPause();

    void onCreate(@Nullable Bundle savedInstanceState);

    @Nullable
    View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

    Ability getStarter();

    void setContext(Context context);
}
