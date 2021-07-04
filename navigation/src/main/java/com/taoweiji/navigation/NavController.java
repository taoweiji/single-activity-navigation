package com.taoweiji.navigation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
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
    @NonNull
    private final NavOptions defaultNavOptions;
    private final GenerateRoute onGenerateRoute;
    final Context context;
    static Map<Integer, FragmentAbility> fragmentAbilityMap = new WeakHashMap<>();
    private final LifecycleEventObserver activityEventObserver;

    private NavController(FrameLayout container, Map<String, AbilityRouteBuilder> routes, @Nullable NavOptions defaultNavOptions, GenerateRoute onGenerateRoute, Destination defaultDestination) {
        this.viewContainer = new ViewContainer(container);
        if (routes != null) {
            this.routes = routes;
        } else {
            this.routes = new HashMap<>();
        }
        if (defaultNavOptions != null) {
            this.defaultNavOptions = defaultNavOptions;
        } else {
            this.defaultNavOptions = NavOptions.DEFAULT;
        }
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


    private void handleDestination(Destination destination) {
        if (destination.getAbility() != null) {
            return;
        }
        if (onGenerateRoute != null) {
            destination.setAbility(onGenerateRoute.onGenerateRoute(context, destination));
        }
        if (destination.getAbility() != null) {
            return;
        }
        if (destination.getName() != null) {
            AbilityRouteBuilder builder = routes.get(destination.getName());
            if (builder != null) {
                destination.setAbility(builder.builder(context));
            }
        }
    }

    public AbilityResultContracts navigate(Destination destination, NavOptions navOptions) {
        destination.setNavOptions(navOptions);
        return navigate(destination);
    }

    public AbilityResultContracts navigate(Destination destination) {
        checkThread();
        handleDestination(destination);
        Ability ability = destination.getAbility();
        if (ability == null) {
            return new AbilityResultContracts();
        }
        Stack<Ability> stack = getStack();
        Ability stackTop = stack.isEmpty() ? null : stack.peek();
        // 如果是在栈顶，仅仅需要处理 arguments
        if (stackTop == ability) {
            ability.performOnNewArguments(destination.getArguments());
            return new AbilityResultContracts();
        }
        ability.navOptions = destination.getNavOptions();
        AbilityResultContracts abilityResultContracts = new AbilityResultContracts();
        if (stackTop != null) {
            stackTop.performOnPause();
        }
        ability.setAbilityResultContracts(abilityResultContracts);
        ability.setContext(context);
        ability.setArguments(destination.getArguments());
        ability.performCreateViewParent(this);
        viewContainer.addAbility(ability);
        ability.performCreateView(null);
        if (stack.contains(ability)) {
            ability.performOnNewArguments(destination.getArguments());
        }
        ability.performOnResume();
        // 如果当前栈内无页面或者页面自定义
        Animation.AnimationListener enterAnimListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                abilityResultContracts.setNavigationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
        enterAnim(ability, stackTop == null).setAnimationListener(enterAnimListener);
        if (stackTop != null && ability.overrideEnterAnim < 0) {
            exitAnim(stackTop, ability.navOptions).setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    stackTop.getDecorView().setTranslationX(0);
                    stackTop.getDecorView().setTranslationY(0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        return abilityResultContracts;
    }

    private void checkThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("必须在主线程执行");
        }
    }

    private Animation enterAnim(Ability ability, boolean forceNoAnimation) {
        // NavController 默认的动画
        int resId = defaultNavOptions.getEnterAnim();
        // navigate 动画
        if (ability.navOptions != null && ability.navOptions.getEnterAnim() != -1) {
            resId = ability.navOptions.getEnterAnim();
        }
        // 覆盖动画
        if (ability.overrideEnterAnim != -1) {
            resId = ability.overrideEnterAnim;
        }
        if (forceNoAnimation) {
            resId = 0;
        }
        Animation animation;
        if (resId == 0 || resId == -1) {
            animation = new TranslateAnimation(0, 0, 0, 0);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), resId);
        }
        ability.getDecorView().startAnimation(animation);
        return animation;
    }

    private Animation exitAnim(Ability ability, NavOptions navOptions) {
        // NavController 默认的动画
        int resId = defaultNavOptions.getExitAnim();
        // navigate 动画
        if (navOptions != null && navOptions.getExitAnim() != -1) {
            resId = navOptions.getExitAnim();
        }
        Animation animation;
        if (resId == 0 || resId == -1) {
            animation = new TranslateAnimation(0, 0, 0, 0);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), resId);
        }
        ability.getDecorView().startAnimation(animation);
        return animation;
    }

    private Animation popEnterAnim(Ability ability, NavOptions navOptions) {
        // NavController 默认的动画
        int resId = defaultNavOptions.getPopEnterAnim();
        // navigate 动画
        if (navOptions != null && navOptions.getPopEnterAnim() != -1) {
            resId = navOptions.getPopEnterAnim();
        }
        Animation animation;
        if (resId == 0 || resId == -1) {
            animation = new TranslateAnimation(0, 0, 0, 0);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), resId);
        }
        ability.getDecorView().startAnimation(animation);
        return animation;
    }

    private Animation popExitAnim(Ability ability) {
        // NavController 默认的动画
        int resId = defaultNavOptions.getPopExitAnim();
        // navigate 动画
        if (ability.navOptions != null && ability.navOptions.getPopExitAnim() != -1) {
            resId = ability.navOptions.getPopExitAnim();
        }
        // 覆盖动画
        if (ability.overrideExitAnim != -1) {
            resId = ability.overrideExitAnim;
        }
        Animation animation;
        if (resId == 0 || resId == -1) {
            animation = new TranslateAnimation(0, 0, 0, 0);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), resId);
        }
        ability.getDecorView().startAnimation(animation);
        return animation;
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
            if (ability != destination.getAbility()) {
                destroyAbility(ability);
            }
        }
        navigate(destination);
    }


    public void popUntil(PopUntil popUntil) {
        while (canBack() && !popUntil.popUntil(getStackTop())) {
            getStackTop().finish();
        }
    }

    public boolean pop() {
        if (canBack()) {
            popInner();
            return true;
        }
        return false;
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
            popInner();
        } else {
            destroyAbility(ability);
        }
    }

    void popInner() {
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
        Animation.AnimationListener popExitAnimListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runnable.run();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        popExitAnim(destroyAbility).setAnimationListener(popExitAnimListener);
        // 如果存在 overrideExitAnim，那么就不显示 popEnterAnim 动画
        if (showAbility != null && destroyAbility.overrideExitAnim < 0) {
            popEnterAnim(showAbility, destroyAbility.navOptions);
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
            if (viewGroup.indexOfChild(ability.getDecorView()) >= 0) {
                viewGroup.removeView(ability.getDecorView());
            }
            viewGroup.addView(ability.getDecorView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        public void removeAbility(Ability ability) {
            viewGroup.removeView(ability.getDecorView());
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
