package com.taoweiji.navigation;

import android.content.Context;
import android.util.Log;
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
        return view == ((Ability) object).getViewParent();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Ability ability = getItem(position);
        ability.prepareCreate(context);
        container.addView(ability.getViewParent());
        return ability;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Ability ability = (Ability) object;
        container.removeView(ability.getViewParent());
    }

    public abstract Ability getItem(int position);
}