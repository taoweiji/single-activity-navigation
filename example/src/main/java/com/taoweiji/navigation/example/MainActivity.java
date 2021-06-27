package com.taoweiji.navigation.example;

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
        Map<String, AbilityRouteBuilder> routes = new HashMap<>();
        routes.put("index", context -> new IndexAbility());
        routes.put("user", context -> new UserAbility());
        routes.put("weather", context -> new MvvmAbility());
        routes.put("fragment", context -> new FragmentAbility(new SimpleFragment()));
        return new NavController.Builder().routes(routes).onGenerateRoute((context, destination) -> {
            if (destination.uri != null && destination.uri.getPath().equals("/hello")) {
                destination.arguments.putString("msg", destination.uri.getQueryParameter("msg"));
                return new UserAbility();
            } else if (destination.uri != null) {
                String name = destination.uri.getPath();
                destination.name = name.substring(1);
            }
            return null;
        }).defaultDestination(Destination.with("index", null));

    }
}
