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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

public class NavController {
    private final Map<String, AbilityRouteBuilder> routes;
    private final FrameLayout navContainer;
    private final NavOptions defaultNavOptions;
    private final GenerateRoute onGenerateRoute;
    final Context context;
    private final OnBackPressedCallback onBackPressedCallback;
    static Map<Integer, FragmentAbility> fragmentAbilityMap = new WeakHashMap<>();

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


    private void destroy() {
        while (navContainer.getChildCount() > 0) {
            pop(false, true);
        }
    }

    public void registerRoute(String name, AbilityRouteBuilder abilityRouteBuilder) {
        routes.put(name, abilityRouteBuilder);
    }

    void finish(Ability ability) {
        int index = navContainer.indexOfChild(ability.getViewParent());
        if (index < 0) return;
        if (index == navContainer.getChildCount() - 1) {
            pop();
        } else {
            ability.onDestroy();
            navContainer.removeView(ability.getViewParent());
        }
    }

    public void sendAbilityEvent(Message message) {
        Runnable runnable = () -> {
            for (int i = 0; i < navContainer.getChildCount(); i++) {
                AbilityViewParent viewParent = (AbilityViewParent) navContainer.getChildAt(i);
                viewParent.getAbility().onAbilityEvent(message);
            }
        };
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public interface GenerateRoute {
        Ability onGenerateRoute(Destination destination);
    }

    private NavController(FrameLayout container, Map<String, AbilityRouteBuilder> routes, NavOptions defaultNavOptions, GenerateRoute onGenerateRoute) {
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
        this.onBackPressedCallback = new OnBackPressedCallback(false) {

            @Override
            public void handleOnBackPressed() {
                if (canBack()) {
                    pop();
                }
            }
        };
        FragmentActivity fragmentActivity = ((FragmentActivity) context);
        fragmentActivity.getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
        fragmentActivity.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event == Lifecycle.Event.ON_DESTROY) {
                destroy();
            }
        });
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

    public AbilityResultContracts navigate(Destination destination, boolean animation) {
        if (destination.ability == null) {
            if (onGenerateRoute != null) {
                destination.ability = onGenerateRoute.onGenerateRoute(destination);
            }
            if (destination.ability == null && destination.name != null) {
                AbilityRouteBuilder builder = routes.get(destination.name);
                if (builder != null) {
                    destination.ability = builder.builder(context);
                }
            }
        }
        int navContainerWidth = navContainer.getWidth();
        if (destination.ability != null) {
            Ability ability = destination.ability;
            ability.context = context;
            ability.setArguments(destination.arguments);
            ability.onCreate(null);
            AbilityViewParent abilityViewParent = ability.performCreateViewParent(this);
            navContainer.addView(abilityViewParent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ability.performCreateView(null);
            if (navContainer.getChildCount() > 0 && animation) {
                move(abilityViewParent, 200, navContainerWidth, 0f);
            }
            ability.onStart();
            ability.onResume();

            if (navContainer.getChildCount() > 1) {
                AbilityViewParent inviableView = (AbilityViewParent) navContainer.getChildAt(navContainer.getChildCount() - 2);
                if (animation) {
                    move(inviableView, 400, 0f, -navContainerWidth).addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation, boolean isReverse) {
                            inviableView.setTranslationX(0);
                            inviableView.setTranslationY(0);
                        }
                    });
                }
                inviableView.getAbility().onStop();
                inviableView.getAbility().onPause();
            }
        }

        onBackPressedCallback.setEnabled(canBack());
        // TODO
        return new AbilityResultContracts();
    }

    public void pushAnimation(View view) {

    }

    private ObjectAnimator move(View view, long duration, float start, float end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", new float[]{start, end});
        animator.setDuration(duration);
        animator.start();
        return animator;
    }

    public void popUntil(PopUntil popUntil) {

    }

    public void pop() {
        pop(true, false);
    }

    public int stackCount() {
        return navContainer.getChildCount();
    }

    public void pop(boolean animation, boolean popRoot) {
        if (canBack() || (popRoot && stackCount() > 0)) {
            int x = navContainer.getWidth();
            AbilityViewParent destroyView = (AbilityViewParent) navContainer.getChildAt(navContainer.getChildCount() - 1);
            AbilityViewParent showView = null;
            if (stackCount() >= 2) {
                showView = (AbilityViewParent) navContainer.getChildAt(navContainer.getChildCount() - 2);
            }

            if (animation) {
                move(destroyView, 200, 0f, x).addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navContainer.removeView(destroyView);
                        onBackPressedCallback.setEnabled(canBack());
                    }
                });
                if (showView != null) {
                    move(showView, 200, -x, 0f);
                }
            } else {
                navContainer.removeView(destroyView);
                move(showView, 0, -x, 0f);
            }
            destroyView.getAbility().onStop();
            destroyView.getAbility().onPause();
            destroyView.getAbility().onDestroy();
            if (showView != null) {
                showView.getAbility().onStop();
                showView.getAbility().onResume();
            }
        }
        onBackPressedCallback.setEnabled(canBack());
    }

    public boolean canBack() {
        return navContainer.getChildCount() > 1;
    }

    public void popAndPush(Destination destination) {
        pop(false, true);
        navigate(destination);
    }

    public void pushAndRemoveUntil(Destination destination) {

    }

    public Ability peek() {
        AbilityViewParent viewParent = (AbilityViewParent) navContainer.getChildAt(navContainer.getChildCount() - 1);
        return viewParent.getAbility();
    }

    interface PopUntil {
        boolean popUntil(AbilityRouteBuilder builder);
    }

    public Map<String, AbilityRouteBuilder> getRoutes() {
        return routes;
    }

    public void getStartCaller(Fragment fragment) {

    }

    public void getStartCaller(Ability ability) {

    }

    public static class Builder {
        Map<String, AbilityRouteBuilder> routes = new HashMap<>();
        NavOptions defaultNavOptions;
        GenerateRoute onGenerateRoute;

        public Builder routes(Map<String, AbilityRouteBuilder> routes) {
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
            return new NavController(container, routes, defaultNavOptions, onGenerateRoute);
        }
    }

}
