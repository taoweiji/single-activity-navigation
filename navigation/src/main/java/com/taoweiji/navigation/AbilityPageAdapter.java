package com.taoweiji.navigation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public abstract class AbilityPageAdapter extends PagerAdapter {
    private final Context context;

    public AbilityPageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public abstract int getCount();

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((Ability) object).getDecorView();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Ability ability = getItem(position);
        ability.prepareCreate(context, null);
        container.addView(ability.getDecorView());
        return ability;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Ability ability = (Ability) object;
        container.removeView(ability.getDecorView());
    }

    public abstract Ability getItem(int position);
}