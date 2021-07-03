package com.taoweiji.navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
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
    private final ViewContainer viewContainer;
    private final NavOptions defaultNavOptions;
    private final GenerateRoute onGenerateRoute;
    final Context context;
    static Map<Integer, FragmentAbility> fragmentAbilityMap = new WeakHashMap<>();
    private final LifecycleEventObserver activityEventObserver;

    private NavController(FrameLayout container, Map<String, AbilityRouteBuilder> routes, NavOptions defaultNavOptions, GenerateRoute onGenerateRoute, Destination defaultDestination) {
        if (defaultNavOptions == null) {
            defaultNavOptions = NavOptions.DEFAULT;
        }
        if (routes == null) {
            routes = new HashMap<>();
        }
        this.viewContainer = new ViewContainer(container);
        this.routes = routes;
        this.defaultNavOptions = defaultNavOptions;
        this.onGenerateRoute = onGenerateRoute;
        this.context = container.getContext();
        this.activityEventObserver = (source, event) -> {
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
        };
        getActivity().getLifecycle().addObserver(activityEventObserver);
        if (defaultDestination != null) {
            this.navigate(defaultDestination);
        }
    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
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
            Stack<Ability> stack = getStack();
            for (Ability ability : stack) {
                ability.onEvent(message);
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

    public AbilityResultContracts navigate(Destination destination, boolean animation) {
        handleDestination(destination);
        Ability ability = destination.ability;
        if (ability == null) {
            return new AbilityResultContracts();
        }
        Stack<Ability> stack = getStack();
        Ability stackTop = stack.isEmpty() ? null : stack.peek();
        // 如果是在栈顶，仅仅需要处理 arguments
        if (stackTop == ability) {
            ability.performOnNewArguments(destination.arguments);
            return new AbilityResultContracts();
        }
        AbilityResultContracts abilityResultContracts = new AbilityResultContracts();
        if (stackTop != null) {
            stackTop.performOnPause();
        }
        ability.setAbilityResultContracts(abilityResultContracts);
        ability.setContext(context);
        ability.setArguments(destination.arguments);
        ability.performCreateViewParent(this);
        viewContainer.addAbility(ability);
        ability.performCreateView(null);
        if (stack.contains(ability)) {
            ability.performOnNewArguments(destination.arguments);
        }
        ability.performOnResume();
        if (ability.enterAnim == 0) {
            // TODO
            animation = false;
        }
        if (stackTop != null && animation) {
            int width = viewContainer.getWidth();
            moveAnimation(ability.getViewParent(), 200, width, 0f).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    abilityResultContracts.setNavigationEnd();
                }
            });
            moveAnimation(stackTop.getViewParent(), 400, 0f, -width).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    stackTop.getViewParent().setTranslationX(0);
                    stackTop.getViewParent().setTranslationY(0);
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).post(abilityResultContracts::setNavigationEnd);
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
        return getStack().size();
    }

    public boolean canBack() {
        return getStackCount() > 1;
    }

    public boolean isRootAbility(Ability ability) {
        Stack<Ability> stack = getStack();
        return stack.indexOf(ability) == 0;
    }

    public Ability getStackTop() {
        return getStackTop(0);
    }

    private Ability getStackTop(int dp) {
        Stack<Ability> stack = getStack();
        while (dp > 0 && !stack.isEmpty()) {
            stack.pop();
            dp--;
        }
        return stack.isEmpty() ? null : stack.peek();
    }

    private Stack<Ability> getStack() {
        return viewContainer.getStack();
    }


    public void dispatcherOnBackPressed() {
        if (canBack()) {
            getStackTop().onBackPressed();
        }
    }

    private void destroyAbility(Ability ability) {
        if (ability.isFinishing()) {
            return;
        }
        ability.finished = true;
        ability.finish();
        ability.performOnPause();
        ability.performOnDestroy();
        viewContainer.removeAbility(ability);
    }

    public void relaunch(Destination destination) {
        Stack<Ability> stack = getStack();
        for (Ability ability : stack) {
            if (ability != destination.ability) {
                destroyAbility(ability);
            }
        }
        navigate(destination, false);
    }


    public void popUntil(PopUntil popUntil) {
        while (canBack() && !popUntil.popUntil(getStackTop())) {
            getStackTop().finish();
        }
    }

    public void pop() {
        if (canBack()) {
            popInner(true);
        }
    }

    public interface PopUntil {
        /**
         * 返回 true 终止 pop
         */
        boolean popUntil(Ability ability);
    }

    /**
     * 如果方法只允许 Ability 自己调用
     */
    void finish(Ability ability) {
        Stack<Ability> stack = getStack();
        int index = stack.indexOf(ability);
        if (index < 0) return;
        if (index == stack.size() - 1) {
            popInner(true);
        } else {
            destroyAbility(ability);
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
            viewContainer.removeAbility(destroyAbility);
        };

        if (destroyAbility.exitAnim == 0) {
            // TODO
            animation = false;
        }
        if (animation) {
            moveAnimation(destroyAbility.getViewParent(), 200, 0f, viewContainer.getWidth()).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    runnable.run();
                }
            });
            if (showAbility != null) {
                moveAnimation(showAbility.getViewParent(), 200, -viewContainer.getWidth(), 0f);
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
    public void destroy() {
        Stack<Ability> stack = getStack();
        for (Ability ability : stack) {
            destroyAbility(ability);
        }
        getActivity().getLifecycle().removeObserver(activityEventObserver);
    }


    public Map<String, AbilityRouteBuilder> getRoutes() {
        return routes;
    }

    @Nullable
    public NavController findParentNavController() {
        return findNavController(viewContainer.viewGroup);
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

        public NavController create(FrameLayout container) {
            return new NavController(container, routes, defaultNavOptions, onGenerateRoute, defaultDestination);
        }

        public Builder defaultDestination(Destination defaultDestination) {
            this.defaultDestination = defaultDestination;
            return this;
        }
    }

    private static class ViewContainer {
        private final FrameLayout viewGroup;

        public ViewContainer(FrameLayout viewGroup) {
            viewGroup.removeAllViews();
            this.viewGroup = viewGroup;
        }

        public void addAbility(Ability ability) {
            if (viewGroup.indexOfChild(ability.getViewParent()) >= 0) {
                viewGroup.removeView(ability.getViewParent());
            }
            viewGroup.addView(ability.getViewParent(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        public void removeAbility(Ability ability) {
            viewGroup.removeView(ability.getViewParent());
        }

        public int getWidth() {
            return viewGroup.getWidth();
        }

        public Stack<Ability> getStack() {
            Stack<Ability> stack = new Stack<>();
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                Ability ability = ((AbilityViewParent) viewGroup.getChildAt(i)).getAbility();
                if (!ability.isFinishing()) {
                    stack.add(ability);
                }
            }
            return stack;
        }
    }
}
