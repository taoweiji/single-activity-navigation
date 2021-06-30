package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.taoweiji.navigation.AbilityRouteBuilder;
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.FragmentAbility;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.NavControllerActivity;
import com.taoweiji.navigation.example.mvvm.MvvmAbility;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends NavControllerActivity {

    @Override
    public NavController.Builder createNavControllerBuilder() {

        return new NavController.Builder()
                .registerRoute("index", context -> new IndexAbility())
                .registerRoute("user", context -> new UserAbility())
                .registerRoute("weather", context -> new MvvmAbility())
                .registerRoute("fragment", context -> new FragmentAbility(new SimpleFragment()))
                .onGenerateRoute((context, destination) -> {
                    if (destination.uri != null && destination.uri.getPath().equals("/hello")) {
                        destination.arguments.putString("msg", destination.uri.getQueryParameter("msg"));
                        return new UserAbility();
                    } else if (destination.uri != null) {
                        String name = destination.uri.getPath();
                        destination.name = name.substring(1);
                    }
                    return null;
                }).defaultDestination(Destination.with("index"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openAndroidL();
    }

    private void openAndroidL() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
