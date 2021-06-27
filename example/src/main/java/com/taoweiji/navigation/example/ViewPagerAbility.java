package com.taoweiji.navigation.example;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.AbilityPageAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAbility extends Ability {

    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ability_view_pager, null);
    }

    @Override
    protected void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(findViewById(R.id.toolbar));
        setTitle("在ViewPager使用AbilityPageAdapter");
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        setToolbar(findViewById(R.id.toolbar));
        MyAbilityPageAdapter adapter = new MyAbilityPageAdapter(getContext());
        adapter.items.add(new MyAbility("Ability 0"));
        adapter.items.add(new MyAbility("Ability 1"));
        adapter.items.add(new MyAbility("Ability 2"));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class MyAbility extends Ability {
        private String text;

        public MyAbility(String text) {
            this.text = text;
        }

        @NonNull
        @Override
        protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }

        @Override
        protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.e(text, "onCreate");
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            Log.e(text, "onDestroy");
        }

        @Override
        protected void onResume() {
            super.onResume();
            Log.e(text, "onResume");
        }

        @Override
        protected void onPause() {
            super.onPause();
            Log.e(text, "onPause");
        }

        @Override
        protected void onStop() {
            super.onStop();
            Log.e(text, "onStop");
        }

        @Override
        protected void onStart() {
            super.onStart();
            Log.e(text, "onStart");
        }
    }

    static class MyAbilityPageAdapter extends AbilityPageAdapter {
        final List<Ability> items = new ArrayList<>();

        public MyAbilityPageAdapter(Context context) {
            super(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Ability getItem(int position) {
            return items.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Ability " + position;
        }
    }
}
