package com.taoweiji.navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class NavController {
    private final Map<String, AbilityRouteBuilder> routes;
    private final FrameLayout navContainer;
    private final Stack<Ability> abilityStack = new Stack<>();
    private final NavOptions defaultNavOptions;
    private final GenerateRoute onGenerateRoute;
    final Context context;
    private final OnBackPressedCallback onBackPressedCallback;

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
        // TODO 需要考虑 Fragment
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
        // TODO 需要考虑 Fragment
        return null;
    }

    public static Ability findAbility(Fragment fragment) {

        // TODO 需要考虑 Fragment
//        if (view instanceof AbilityViewParent) {
//            AbilityViewParent abilityViewParent = (AbilityViewParent) view;
//            return abilityViewParent.getAbility();
//        } else {
//            if (view.getParent() instanceof View) {
//                return findAbility((View) view.getParent());
//            }
//        }
        return findAbility(fragment.getView());
    }


    private void destroy() {
        while (!abilityStack.isEmpty()) {
            pop(false, true);
        }
    }

    public void registerRoute(String name, AbilityRouteBuilder abilityRouteBuilder) {
        routes.put(name, abilityRouteBuilder);
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
        if (destination.name != null) {
            AbilityRouteBuilder builder = routes.get(destination.name);
            if (builder != null) {
                destination.ability = builder.builder(context);
            }
        }

        int x = navContainer.getWidth();
        if (destination.ability != null) {
            Ability ability = destination.ability;
            ability.setContext(context);
            ability.setArguments(destination.arguments);
            ability.onCreate(null);
            AbilityViewParent abilityViewParent = ability.performCreateViewParent(this);
            navContainer.addView(abilityViewParent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View abilityView = ability.performCreateView(null);
            ability.onViewCreated(abilityView, null);
            if (animation) {
                move(abilityViewParent, 300, x, 0f);
            }
            ability.onStart();
            ability.onResume();
            abilityStack.add(ability);
        }
        if (navContainer.getChildCount() > 1) {
            View dismissAbilityView = navContainer.getChildAt(navContainer.getChildCount() - 2);
            Ability dismissAbility = abilityStack.get(navContainer.getChildCount() - 1);
            move(dismissAbilityView, 500, 0f, -x).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    dismissAbilityView.setTranslationX(0);
                    dismissAbilityView.setTranslationY(0);
                }
            });
            dismissAbility.onStop();
            dismissAbility.onPause();
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
        return abilityStack.size();
    }

    public void pop(boolean animation, boolean popRoot) {
        if (canBack() || (popRoot && stackCount() > 0)) {
            int x = navContainer.getWidth();
            View dismiss = navContainer.getChildAt(navContainer.getChildCount() - 1);
            Ability dismissAbility = abilityStack.get(navContainer.getChildCount() - 1);
            View show = null;
            Ability showAbility = null;
            if (stackCount() >= 2) {
                show = navContainer.getChildAt(navContainer.getChildCount() - 2);
                showAbility = abilityStack.get(navContainer.getChildCount() - 2);
            }

            if (animation) {
                move(dismiss, 200, 0f, x).addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navContainer.removeView(dismiss);
                        onBackPressedCallback.setEnabled(canBack());
                        abilityStack.remove(dismissAbility);
                    }
                });
                if (show != null) {
                    move(show, 300, -x, 0f);
                }
            } else {
                navContainer.removeView(dismiss);
                abilityStack.remove(dismissAbility);
                move(show, 0, -x, 0f);
            }
            dismissAbility.onStop();
            dismissAbility.onPause();
            dismissAbility.onDestroy();
            if (show != null) {
                showAbility.onStop();
                showAbility.onResume();
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
        return abilityStack.peek();
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
