package com.taoweiji.navigation.example;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.AbilityBuilder;
import com.taoweiji.navigation.AbilityContainer;
import com.taoweiji.navigation.AbilityRouteBuilder;
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavController;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, AbilityRouteBuilder> routes = new HashMap<>();
        routes.put("user", context -> new UserAbility());
        NavController nav = new NavController.Builder().routes(routes).onGenerateRoute(new NavController.GenerateRoute() {
            @Override
            public Ability onGenerateRoute(Destination destination) {
                return null;
            }
        }).create(this, R.id.container);
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        nav.navigate(new UserAbility(), bundle);
    }
}