package com.taoweiji.navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class NavController {
    private final Map<String, AbilityRouteBuilder> routes;
    private final FrameLayout navContainer;
    private final NavOptions defaultNavOptions;
    private final GenerateRoute onGenerateRoute;
    final Context context;
    static Map<Integer, FragmentAbility> fragmentAbilityMap = new WeakHashMap<>();

    private NavController(FrameLayout container, Map<String, AbilityRouteBuilder> routes, NavOptions defaultNavOptions, GenerateRoute onGenerateRoute, Destination defaultDestination) {
        if (defaultNavOptions == null) {
            defaultNavOptions = NavOptions.DEFAULT;
        }
        if (routes == null) {
            routes = new HashMap<>();
        }
        this.navContainer = container;
        this.routes = routes;
        this.defaultNavOptions = defaultNavOptions;
        this.onGenerateRoute = onGenerateRoute;
        this.context = container.getContext();
        FragmentActivity fragmentActivity = ((FragmentActivity) context);
        fragmentActivity.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event == Lifecycle.Event.ON_DESTROY) {
                destroy();
            } else if (event == Lifecycle.Event.ON_RESUME) {
                Ability ability = getStackTop();
                if (ability != null) {
                    ability.performOnResume();
                }
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                Ability ability = getStackTop();
                if (ability != null) {
                    ability.performOnPause();
                }
            }
        });
        if (defaultDestination != null) {
            this.navigate(defaultDestination);
        }
    }

    public static NavController findNavController(Fragment fragment) {
        return findNavController(fragment.getView());
    }

    public static NavController findNavController(View view) {
        if (view instanceof AbilityViewParent) {
            AbilityViewParent abilityViewParent = (AbilityViewParent) view;
            return abilityViewParent.getNavController();
        } else {
            if (view.getParent() instanceof View) {
                return findNavController((View) view.getParent());
            }
        }
        return null;
    }

    public static Ability findAbility(View view) {
        if (view instanceof AbilityViewParent) {
            AbilityViewParent abilityViewParent = (AbilityViewParent) view;
            return abilityViewParent.getAbility();
        } else {
            if (view.getParent() instanceof View) {
                return findAbility((View) view.getParent());
            }
        }
        return null;
    }

    public static Ability findAbility(Fragment fragment) {
        FragmentAbility ability = fragmentAbilityMap.get(fragment.hashCode());
        if (ability != null) {
            return ability;
        }
        return findAbility(fragment.getView());
    }

    static void addFragmentAbility(FragmentAbility fragmentAbility) {
        fragmentAbilityMap.put(fragmentAbility.getFragment().hashCode(), fragmentAbility);
    }

    public void sendAbilityEvent(Message message) {
        Runnable runnable = () -> {
            for (int i = 0; i < navContainer.getChildCount(); i++) {
                getAbilityViewParent(i).getAbility().onEvent(message);
            }
        };
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public interface GenerateRoute {
        Ability onGenerateRoute(Context context, Destination destination);
    }

    public AbilityResultContracts navigate(Uri uri) {
        return navigate(Destination.with(uri, null));
    }

    public AbilityResultContracts navigate(String name) {
        return navigate(name, null);
    }

    public AbilityResultContracts navigate(String name, Bundle arguments) {
        return navigate(Destination.with(name, arguments));
    }

    public AbilityResultContracts navigate(Fragment fragment) {
        return navigate(fragment, null);
    }

    public AbilityResultContracts navigate(Fragment fragment, Bundle arguments) {
        return navigate(Destination.with(new FragmentAbility(fragment), arguments));
    }

    public AbilityResultContracts navigate(Ability ability) {
        return navigate(ability, null);
    }

    public AbilityResultContracts navigate(Ability ability, Bundle arguments) {
        return navigate(Destination.with(ability, arguments));
    }

    public AbilityResultContracts navigate(Destination destination) {
        return navigate(destination, true);
    }

    private void handleDestination(Destination destination) {
        if (destination.ability != null) {
            return;
        }
        if (onGenerateRoute != null) {
            destination.ability = onGenerateRoute.onGenerateRoute(context, destination);
        }
        if (destination.ability != null) {
            return;
        }
        if (destination.name != null) {
            AbilityRouteBuilder builder = routes.get(destination.name);
            if (builder != null) {
                destination.ability = builder.builder(context);
            }
        }
    }

//    public void popAndNavigate(Destination destination) {
//        AbilityViewParent ability = stackCount() == 0 ? null : getAbilityViewParent(stackCount() - 1);
//        navigate(destination).registerListener(() -> {
//            if (ability != null) {
//                ability.getAbility().finish();
//            }
//        });
//    }

    public AbilityResultContracts navigate(Destination destination, boolean animation) {
        handleDestination(destination);
        AbilityResultContracts abilityResultContracts;
        if (destination.ability != null) {
            if (destination.ability == getStackTop()) {
                destination.ability.performOnNewArguments(destination.arguments);
                abilityResultContracts = new AbilityResultContracts();
            } else if (navContainer.indexOfChild(destination.ability.getViewParent()) >= 0) {
                getStackTop().performOnPause();
                navContainer.removeView(destination.ability.getViewParent());
                navContainer.addView(destination.ability.getViewParent());
                destination.ability.performOnResume();
                destination.ability.performOnNewArguments(destination.arguments);
                abilityResultContracts = new AbilityResultContracts();
            } else {
                abilityResultContracts = pushInner(destination.ability, destination.arguments, animation);
            }
        } else {
            abilityResultContracts = new AbilityResultContracts();
        }
        return abilityResultContracts;
    }

    private AbilityResultContracts pushInner(Ability ability, Bundle arguments, boolean animation) {
        Ability stackTop = getStackTop();
        if (stackTop != null) {
            stackTop.performOnPause();
        }
        AbilityResultContracts abilityResultContracts = new AbilityResultContracts();
        int navContainerWidth = navContainer.getWidth();
        ability.setAbilityResultContracts(abilityResultContracts);
        ability.setContext(context);
        ability.setArguments(arguments);
        AbilityViewParent abilityViewParent = ability.performCreateViewParent(this);
        // 入栈
        navContainer.addView(abilityViewParent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 触发创建
        ability.performCreateView(null);
        // 动画
        if (navContainer.getChildCount() > 0 && animation) {
            moveAnimation(abilityViewParent, 200, navContainerWidth, 0f).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    abilityResultContracts.setNavigationEnd();
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).post(abilityResultContracts::setNavigationEnd);
        }
        ability.performOnResume();
        if (stackTop != null) {
            if (animation) {
                moveAnimation(stackTop.getViewParent(), 400, 0f, -navContainerWidth).addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        stackTop.getViewParent().setTranslationX(0);
                        stackTop.getViewParent().setTranslationY(0);
                    }
                });
            }
        }
        return abilityResultContracts;
    }

    private ObjectAnimator moveAnimation(View view, long duration, float start, float end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", start, end);
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    public int getStackCount() {
        int count = 0;
        for (int i = 0; i < navContainer.getChildCount(); i++) {
            if (getAbilityViewParent(i).getAbility().isFinishing()) {
                continue;
            }
            count++;
        }
        return count;
    }

    public boolean canBack() {
        return navContainer.getChildCount() > 1;
    }

    public boolean isRootAbility(Ability ability) {
        return navContainer.indexOfChild(ability.getViewParent()) == 0;
    }

    public Ability getStackTop() {
        return getStackTop(0);
    }

    private Ability getStackTop(int dp) {
        for (int i = navContainer.getChildCount() - 1; i >= 0; i--) {
            AbilityViewParent viewParent = getAbilityViewParent(i);
            if (viewParent.getAbility().isFinishing()) {
                continue;
            }
            if (dp > 0) {
                dp--;
                continue;
            }
            return viewParent.getAbility();
        }
        return null;
    }


    public void dispatcherOnBackPressed() {
        if (canBack()) {
            getStackTop().onBackPressed();
        }
    }

    private AbilityViewParent getAbilityViewParent(int index) {
        return (AbilityViewParent) navContainer.getChildAt(index);
    }

    public void relaunch(Destination destination) {
        destroy();
        navigate(destination, false);
        // TODO 需要考虑首页 relaunch
    }


    public void popUntil(PopUntil popUntil) {
        while (popUntil.popUntil(getStackTop())) {
            getStackTop().finish();
        }
        // TODO
    }

    interface PopUntil {
        boolean popUntil(Ability ability);
    }

    /**
     * 如果方法只允许 Ability 自己调用
     */
    void finish(Ability ability) {
        int index = navContainer.indexOfChild(ability.getViewParent());
        // 如果不存在就直接返回
        if (index < 0) return;
        if (index == navContainer.getChildCount() - 1) {
            // 如果是最后一个是有默认的动画
            popInner(true);
        } else {
            // 如果不是最后一位，就直接销毁
            ability.performOnDestroy();
            navContainer.removeView(ability.getViewParent());
        }
    }

    void popInner(boolean animation) {
        if (getStackCount() == 0) {
            return;
        }
        Ability destroyAbility = getStackTop();
        Ability showAbility = getStackTop(1);
        destroyAbility.performOnPause();
        if (showAbility != null) {
            showAbility.performOnResume();
        }
        Runnable runnable = () -> {
            destroyAbility.performOnDestroy();
            navContainer.removeView(destroyAbility.getViewParent());
        };
        if (animation) {
            int containerWidth = navContainer.getWidth();
            moveAnimation(destroyAbility.getViewParent(), 200, 0f, containerWidth).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    runnable.run();
                }
            });
            if (showAbility != null) {
                moveAnimation(showAbility.getViewParent(), 200, -containerWidth, 0f);
            }
        } else {
            runnable.run();
            if (showAbility != null) {
                showAbility.getViewParent().setTranslationX(0);
                showAbility.getViewParent().setTranslationY(0);
            }
        }
    }

    /**
     * 销毁所有页面，如果是栈顶需要调用 performPause
     */
    private void destroy() {
        for (int i = navContainer.getChildCount() - 1; i >= 0; i--) {
            Ability ability = getAbilityViewParent(i).getAbility();
            if (ability.isFinishing()) {
                continue;
            }
            ability.finished = true;
            ability.finish();
            ability.performOnPause();
            ability.performOnDestroy();
        }
        navContainer.removeAllViews();
    }


    public Map<String, AbilityRouteBuilder> getRoutes() {
        return routes;
    }


    public static class Builder {
        Map<String, AbilityRouteBuilder> routes = new HashMap<>();
        NavOptions defaultNavOptions;
        GenerateRoute onGenerateRoute;
        Destination defaultDestination;

        public Builder registerRoutes(Map<String, AbilityRouteBuilder> routes) {
            this.routes.putAll(routes);
            return this;
        }

        public Builder registerRoute(String name, AbilityRouteBuilder abilityRouteBuilder) {
            this.routes.put(name, abilityRouteBuilder);
            return this;
        }

        public Builder defaultNavOptions(NavOptions defaultNavOptions) {
            this.defaultNavOptions = defaultNavOptions;
            return this;
        }

        public Builder onGenerateRoute(GenerateRoute onGenerateRoute) {
            this.onGenerateRoute = onGenerateRoute;
            return this;
        }

        public NavController create(Activity activity, @IdRes int container) {
            return create(activity.findViewById(container));
        }

        public NavController create(FrameLayout container) {
            return new NavController(container, routes, defaultNavOptions, onGenerateRoute, defaultDestination);
        }

        public Builder defaultDestination(Destination defaultDestination) {
            this.defaultDestination = defaultDestination;
            return this;
        }
    }
}
