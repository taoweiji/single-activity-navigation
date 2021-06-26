package com.taoweiji.navigation.example;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.taoweiji.navigation.FragmentAbility;
import com.taoweiji.navigation.AbilityRouteBuilder;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.example.mvvm.WeatherAbility;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, AbilityRouteBuilder> routes = new HashMap<>();
        routes.put("user", context -> new UserAbility());
        routes.put("weather", context -> new WeatherAbility());
        NavController nav = new NavController.Builder().routes(routes).onGenerateRoute(destination -> {
            if (destination.uri != null && destination.uri.getPath().equals("/hello")) {
                destination.arguments.putString("msg", destination.uri.getQueryParameter("msg"));
                return new UserAbility();
            }
            return null;
        }).create(this, R.id.container);
//        nav.navigate("user", new BundleBuilder().put("id", 0).build());
        nav.navigate("weather", null);
    }
}
