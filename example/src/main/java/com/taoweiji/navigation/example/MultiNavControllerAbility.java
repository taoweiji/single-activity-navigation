package com.taoweiji.navigation.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.AbilityPageAdapter;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavController;

import org.jetbrains.annotations.NotNull;

public class MultiNavControllerAbility extends Ability {
    private TabAbility[] abilities;
    private ViewPager viewPager;

    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ability_multi_nav_controller, null);
    }

    @Override
    protected void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        this.abilities = new TabAbility[]{new TabAbility("主页"), new TabAbility("搜索"), new TabAbility("通知"), new TabAbility("我的")};
        viewPager.setAdapter(new AbilityPageAdapter(getContext()) {

            @Override
            public int getCount() {
                return abilities.length;
            }

            @Override
            public Ability getItem(int position) {
                return abilities[position];
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.navigation_notifications:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.navigation_me:
                        viewPager.setCurrentItem(3, false);
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        TabAbility ability = abilities[viewPager.getCurrentItem()];
        if (ability.nav.canBack()) {
            ability.nav.dispatcherOnBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    static class TabAbility extends Ability {
        String title;
        private NavController nav;

        public TabAbility(String title) {
            this.title = title;
        }

        @Override
        protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            FrameLayout layout = new FrameLayout(getContext());
            this.nav = new NavController.Builder()
                    .defaultDestination(Destination.with(new MyAbility(title), null))
                    .create(layout);
            return layout;
        }
    }

    static class MyAbility extends Ability {
        String title;

        public MyAbility(String title) {
            this.title = title;
        }

        @Override
        protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            createDefaultToolbar();
            setToolbarBackgroundColor(Color.WHITE);
            setStatusBarColor(Color.WHITE);
            setStatusBarTextStyle(StatusBarTextStyle.DARK);
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            int index = getArguments().getInt("index", 0);
            textView.setText("跳转页面");
            setTitle(title + " " + index);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findNavController().navigate(new MyAbility(title), new BundleBuilder().put("index", index + 1).build());
                }
            });
            return textView;
        }
    }
}
