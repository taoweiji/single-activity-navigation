package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.ViewUtils;

import org.jetbrains.annotations.NotNull;

public class MultiNavControllerAbility extends Ability {
    private final TabAbility[] abilities = new TabAbility[]{
            new TabAbility("主页"),
            new TabAbility("搜索"),
            new TabAbility("通知"),
            new TabAbility("我的")};

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
        FrameLayout container = findViewById(R.id.container);
        this.tabNav = new NavController.Builder().defaultDestination(Destination.with(abilities[currentItem])).create(container);
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
        TabAbility ability = abilities[currentItem];
        if (ability.getNav().canBack()) {
            ability.getNav().dispatcherOnBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        for (TabAbility it : abilities) {
            if (it.getNav() != null) {
                it.getNav().destroy();
            }
        }
        tabNav.destroy();
        super.onDestroy();
    }

    static class TabAbility extends Ability {
        String title;
        private NavController nav;

        public TabAbility(String title) {
            this.title = title;
        }

        public NavController getNav() {
            return nav;
        }

        @Override
        protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            FrameLayout layout = new FrameLayout(getContext());
            this.nav = new NavController.Builder().defaultDestination(Destination.with(new MyAbility(title))).create(layout);
            return layout;
        }

        @Override
        protected Toolbar createDefaultToolbar() {
            return null;
        }
    }

    static class MyAbility extends Ability {
        String title;

        public MyAbility(String title) {
            this.title = title;
        }

        @Override
        protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            getToolbar().setElevation(ViewUtils.dp2px(getContext(), 2));
            setToolbarBackgroundColor(Color.WHITE);
            RelativeLayout layout = new RelativeLayout(getContext());
            layout.setGravity(Gravity.CENTER);
            Button button = new Button(getContext());
            layout.addView(button);
            int index = getArguments().getInt("index", 0);
            button.setText("跳转页面");
            setTitle(title + " " + index);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findNavController().navigate(new MyAbility(title), new BundleBuilder().put("index", index + 1).build());
                }
            });
            return layout;
        }
    }
}
