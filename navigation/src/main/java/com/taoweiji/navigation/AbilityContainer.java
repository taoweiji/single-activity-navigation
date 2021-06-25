package com.taoweiji.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;

public abstract class AbilityContainer implements Ability, ActivityResultCaller {
    LifecycleRegistry mLifecycleRegistry;
    private Context context;
    private Bundle arguments;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Bundle getArguments() {
        return arguments;
    }

    @Override
    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    private void initLifecycle() {
        mLifecycleRegistry = new LifecycleRegistry(this);
//        mSavedStateRegistryController = SavedStateRegistryController.create(this);
        // The default factory depends on the SavedStateRegistry so it
        // needs to be reset when the SavedStateRegistry is reset
//        mDefaultFactory = null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract, @NonNull ActivityResultCallback<O> callback) {
        return null;
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract, @NonNull ActivityResultRegistry registry, @NonNull ActivityResultCallback<O> callback) {
        return null;
    }

    @Override
    public Ability getStarter() {
        return null;
    }

    public void preGenerate(Context context) {

    }
}
