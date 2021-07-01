package com.taoweiji.navigation.example;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavController;

import org.jetbrains.annotations.NotNull;

public class BottomNavigationViewAbility extends Ability {
    private TabAbility[] abilities;
    private int currentItem = 0;
    private NavController tabNav;

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ability_multi_nav_controller, null);
    }

    @Override
    protected Toolbar createDefaultToolbar() {
        return null;
    }

    @Override
    protected void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        abilities = new TabAbility[]{
                new TabAbility("主页"),
                new TabAbility("搜索"),
                new TabAbility("通知"),
                new TabAbility("我的")
        };
        FrameLayout container = findViewById(R.id.container);
        tabNav = new NavController.Builder().defaultDestination(Destination.with(abilities[currentItem])).create(container);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    currentItem = 0;
                    break;
                case R.id.navigation_dashboard:
                    currentItem = 1;
                    break;
                case R.id.navigation_notifications:
                    currentItem = 2;
                    break;
                case R.id.navigation_me:
                    currentItem = 3;
                    break;
            }
            tabNav.navigate(Destination.with(abilities[currentItem]), false);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        tabNav.destroy();
        super.onDestroy();
    }

    static class TabAbility extends Ability {
        String title;

        public TabAbility(String title) {
            this.title = title;
        }

        @Override
        protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setDefaultDisplayHomeAsUpEnabled(false);

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            setTitle(title);
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(title);
            linearLayout.addView(textView);
            Button button = new Button(getContext());
            button.setText("跳转");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findNavController().findParentNavController().navigate(new SimpleFragment());
                }
            });
            linearLayout.addView(button);
            return linearLayout;
        }
    }
}
