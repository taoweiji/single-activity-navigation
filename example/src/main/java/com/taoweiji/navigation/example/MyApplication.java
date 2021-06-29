package com.taoweiji.navigation.example;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

public class MyApplication extends Application {
    public static StringBuilder logs = new StringBuilder();

    @Override
    public void onCreate() {
        super.onCreate();
        Navigation.registerAbilityLifecycleCallbacks(new Navigation.AbilityLifecycleCallbacks() {
            @Override
            public void onAbilityCreated(@NonNull @NotNull Ability ability, @Nullable  Bundle savedInstanceState) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onCreated"));
            }

            @Override
            public void onAbilityStarted(@NonNull @NotNull Ability ability) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onStart"));
            }

            @Override
            public void onAbilityResumed(@NonNull @NotNull Ability ability) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onResume"));
            }

            @Override
            public void onAbilityPaused(@NonNull @NotNull Ability ability) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onPause"));
            }

            @Override
            public void onAbilityStopped(@NonNull @NotNull Ability ability) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onStop"));
            }

            @Override
            public void onAbilityDestroyed(@NonNull @NotNull Ability ability) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onDestroy"));
            }

            @Override
            public void onAbilityViewCreated(@NonNull Ability ability) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onViewCreated"));
            }

            @Override
            public void onAbilityCreateViewed(@NonNull @NotNull Ability ability) {
                logs.append(String.format("%s %s\n", ability.getClass().getSimpleName(), "onCreateView"));
            }
        });
    }
}
