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

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class NavController {
    private final Map<String, AbilityRouteBuilder> routes;
    private final FrameLayout container;
    private final Stack<Ability> abilityStack = new Stack<>();
    private final NavOptions defaultNavOptions;
    private final GenerateRoute onGenerateRoute;
    private final Context context;
    private final OnBackPressedCallback onBackPressedCallback;

    public static NavController findNavController(View view) {
        if (view instanceof AbilityViewGroup) {
            AbilityViewGroup abilityViewGroup = (AbilityViewGroup) view;
            return abilityViewGroup.getNavController();
        } else {
            if (view.getParent() instanceof View) {
                return findNavController((View) view.getParent());
            }
        }
        return null;
    }

    public void destroy() {

    }

    public void registerRoute(String name, AbilityRouteBuilder abilityRouteBuilder) {
        routes.put(name, abilityRouteBuilder);
    }

    public interface GenerateRoute {
        Ability onGenerateRoute(Destination destination);
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

    private NavController(FrameLayout container, Map<String, AbilityRouteBuilder> routes, NavOptions defaultNavOptions, GenerateRoute onGenerateRoute) {
        if (defaultNavOptions == null) {
            defaultNavOptions = NavOptions.DEFAULT;
        }
        if (routes == null) {
            routes = new HashMap<>();
        }
        this.container = container;
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
    }


    public AbilityResultContracts navigate(Uri uri) {
        return navigate(Destination.with(uri, null));
    }

    public AbilityResultContracts navigate(String name, Bundle arguments) {
        return navigate(Destination.with(name, arguments, null));
    }

    public AbilityResultContracts navigate(Fragment fragment, Bundle arguments) {
        return navigate(Destination.with(new AbilityFragmentContainer(fragment), arguments, null));
    }

    public AbilityResultContracts navigate(Ability ability, Bundle arguments) {
        return navigate(Destination.with(ability, arguments, null));
    }

    public AbilityResultContracts navigate(Fragment fragment) {
        return navigate(fragment, null);
    }

    public AbilityResultContracts navigate(Ability ability) {
        return navigate(ability, null);
    }

    private static class AbilityViewGroup extends FrameLayout {
        private NavController navController;

        public AbilityViewGroup(@NonNull Context context, NavController navController) {
            super(context);
            this.navController = navController;
        }

        public NavController getNavController() {
            return navController;
        }
    }

    public AbilityResultContracts navigate(Destination destination) {
        return navigate(destination, true);
    }

    public AbilityResultContracts navigate(Destination destination, boolean animation) {
        int x = container.getWidth();
        if (destination.ability != null) {
            Ability ability = destination.ability;
            ability.setContext(context);
            ability.setArguments(destination.arguments);
            AbilityViewGroup abilityViewGroup = new AbilityViewGroup(context, this);
            container.addView(abilityViewGroup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ability.onCreate(null);
            View view = ability.onCreateView(LayoutInflater.from(context), abilityViewGroup, null);
            abilityViewGroup.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ability.onViewCreated(view == null ? new View(context) : view, null);
            if (animation) {
                move(abilityViewGroup, 300, x, 0f);
            }
            ability.onStart();
            ability.onResume();
            abilityStack.add(ability);
        }
        if (container.getChildCount() > 1) {
            View dismiss = container.getChildAt(container.getChildCount() - 2);
            Ability dismissAbility = abilityStack.get(container.getChildCount() - 1);
            move(dismiss, 500, 0f, -x);
            dismissAbility.onStop();
            dismissAbility.onPause();
        }
        onBackPressedCallback.setEnabled(canBack());
        // TODO
        return null;
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
            int x = container.getWidth();
            View dismiss = container.getChildAt(container.getChildCount() - 1);
            Ability dismissAbility = abilityStack.get(container.getChildCount() - 1);
            View show = null;
            Ability showAbility = null;
            if (stackCount() >= 2) {
                show = container.getChildAt(container.getChildCount() - 2);
                showAbility = abilityStack.get(container.getChildCount() - 2);
            }

            if (animation) {
                move(dismiss, 200, 0f, x).addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        container.removeView(dismiss);
                        onBackPressedCallback.setEnabled(canBack());
                        abilityStack.remove(dismissAbility);
                    }
                });
                if (show != null) {
                    move(show, 300, -x, 0f);
                }
            } else {
                container.removeView(dismiss);
                abilityStack.remove(dismissAbility);
                move(show, 0, -x, 0f);
            }
            dismissAbility.onStop();
            dismissAbility.onPause();
            if (show != null) {
                showAbility.onStop();
                showAbility.onResume();
            }
        }
        onBackPressedCallback.setEnabled(canBack());
    }

    public boolean canBack() {
        return container.getChildCount() > 1;
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
}
