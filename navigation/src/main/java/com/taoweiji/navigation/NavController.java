package com.taoweiji.navigation;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class NavController {
    private final Map<String, AbilityRouteBuilder> routes;
    private final FrameLayout container;
    private final NavOptions defaultNavOptions;
    private final GenerateRoute onGenerateRoute;
    private final Context context;

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

    public interface GenerateRoute {
        Ability onGenerateRoute(Destination destination);
    }

    public static class Builder {
        Map<String, AbilityRouteBuilder> routes = new HashMap<>();
        NavOptions defaultNavOptions;
        GenerateRoute onGenerateRoute;

        public Builder routes(Map<String, AbilityRouteBuilder> routes) {
            this.routes = routes;
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
        if (destination.ability != null) {
            Ability ability = destination.ability;
            ability.setContext(context);
            ability.setArguments(destination.arguments);
            AbilityViewGroup abilityViewGroup = new AbilityViewGroup(context, this);
            container.addView(abilityViewGroup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ability.setNavController(this);
            ability.onCreate(null);
            View view = ability.onCreateView(LayoutInflater.from(context), abilityViewGroup, null);
            abilityViewGroup.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ability.onViewCreated(view == null ? new View(context) : view, null);
        }


        // TODO
        return null;
    }

    public void popUntil(PopUntil popUntil) {

    }

    public void pop() {
        if (container.getChildCount() > 1) {
            container.removeViewAt(container.getChildCount() - 1);
        }
    }

    public void popAndPush(Destination destination) {

    }

    public void pushAndRemoveUntil(Destination destination) {

    }


    public void pop(int count) {

    }


    interface PopUntil {
        boolean popUntil(AbilityRouteBuilder builder);
    }

    public AbilityResultContracts registerProvider(Navigator navigator) {
        return null;
    }
}
