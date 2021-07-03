package com.taoweiji.navigation.example;

import android.os.Bundle;

import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.FragmentAbility;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.NavControllerActivity;
import com.taoweiji.navigation.NavOptions;
import com.taoweiji.navigation.example.mvvm.MvvmAbility;


public class MainActivity extends NavControllerActivity {

    @Override
    public NavController.Builder createNavControllerBuilder() {
        return new NavController.Builder()
                .registerRoute("index", context -> new IndexAbility())
                .registerRoute("user", context -> new UserAbility())
                .registerRoute("weather", context -> new MvvmAbility())
                .registerRoute("fragment", context -> new FragmentAbility(new SimpleFragment()))
                .onGenerateRoute((context, destination) -> {
                    if (destination.getUri() != null && destination.getUri().getPath().equals("/hello")) {
                        destination.getArguments().putString("msg", destination.getUri().getQueryParameter("msg"));
                        return new UserAbility();
                    } else if (destination.getUri() != null) {
                        String name = destination.getUri().getPath();
                        destination.setName(name.substring(1));
                    }
                    return null;
                }).defaultDestination(Destination.with("index"))
                .defaultNavOptions(NavOptions.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
