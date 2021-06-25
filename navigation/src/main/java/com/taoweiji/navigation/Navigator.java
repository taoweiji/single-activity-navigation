package com.taoweiji.navigation;

import android.os.Bundle;

/**
 * 跳转的抽象接口，用于实现Fragment、View的跳转
 */
public abstract class Navigator<T extends Ability> {
    public abstract void navigate(T destination, Bundle args, NavOptions navOptions, Bundle navigatorExtras);

    public abstract boolean popBackStack();
}
