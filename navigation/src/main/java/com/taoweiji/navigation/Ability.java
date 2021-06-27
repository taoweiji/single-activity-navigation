package com.taoweiji.navigation;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public abstract class Ability implements ActivityResultCaller, LifecycleOwner {
    LifecycleRegistry mLifecycleRegistry;
    Context context;
    private Bundle arguments;
    private Integer statusBarColor;
    private StatusBarTextStyle statusBarTextStyle;
    private View view;
    private AbilityViewParent viewParent;
    private boolean createViewed;

    public Ability() {
        initLifecycle();
    }

    @NonNull
    public Bundle getArguments() {
        if (arguments == null) {
            arguments = new Bundle();
        }
        return arguments;
    }

    void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    private void initLifecycle() {
        mLifecycleRegistry = new LifecycleRegistry(this);
    }

    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @CallSuper
    protected void onStart() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @CallSuper
    protected void onResume() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        if (statusBarColor != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColorInner(statusBarColor);
        }
        if (statusBarTextStyle != null) {
            setStatusBarTextStyleInner(statusBarTextStyle);
        }
    }

    @NonNull
    protected abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @CallSuper
    protected void onPause() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @CallSuper
    protected void onStop() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @CallSuper
    protected void onDestroy() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract, @NonNull ActivityResultCallback<O> callback) {
        return null;
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract, @NonNull ActivityResultRegistry registry, @NonNull ActivityResultCallback<O> callback) {
        return null;
    }

    public Ability getStarter() {
        return null;
    }

    /**
     * 预创建
     */
    public void prepareCreateView(NavController navController) {
        context = navController.context;
        performCreateViewParent(navController);
        performCreateView(null);
    }

    public Context getContext() {
        return context;
    }

    public final FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }


    @NonNull
    void performCreateView(@Nullable Bundle savedInstanceState) {
        if (createViewed) {
            return;
        }
        createViewed = true;
        view = onCreateView(LayoutInflater.from(context), viewParent, savedInstanceState);
        if (view != null) {
            viewParent.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        this.onViewCreated(view, null);
    }

    public View getView() {
        return view;
    }

    AbilityViewParent performCreateViewParent(NavController navController) {
        if (viewParent == null) {
            viewParent = new AbilityViewParent(context, navController, this);
        }
        return viewParent;
    }


    public enum StatusBarTextStyle {
        LIGHT, DARK
    }

    public Resources getResources() {
        return getActivity().getResources();
    }

    public final CharSequence getText(@StringRes int resId) {
        return getResources().getText(resId);
    }

    public final Drawable getDrawable(@DrawableRes int id) {
        return ResourcesCompat.getDrawable(getResources(), id, null);
    }

    public int getColor(@ColorRes int id) throws Resources.NotFoundException {
        return getResources().getColor(id);
    }

    @NonNull
    public final String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    @NonNull
    public final String getString(@StringRes int resId, @Nullable Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    public void setStatusBarTextStyle(StatusBarTextStyle style) {
        this.statusBarTextStyle = style;
        setStatusBarTextStyleInner(style);
    }

    private void setStatusBarTextStyleInner(StatusBarTextStyle style) {
        this.statusBarTextStyle = style;
        Window window = getActivity().getWindow();
        if (style == StatusBarTextStyle.DARK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
                // TODO
            }
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(int color) {
        this.statusBarColor = color;
        setStatusBarColorInner(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColorInner(int color) {
        Window window = getActivity().getWindow();
        window.setStatusBarColor(color);
    }

    public void setBackgroundColor(@ColorInt int color) {
        viewParent.setBackgroundColor(color);
    }

    public void setBackgroundResource(@DrawableRes int resId) {
        viewParent.setBackgroundResource(resId);
    }

    public void setBackground(Drawable background) {
        viewParent.setBackground(background);
    }

    public NavController findNavController() {
        return NavController.findNavController(viewParent);
    }

    public AbilityViewParent getViewParent() {
        return viewParent;
    }

    public void finish() {
        findNavController().finish(this);
    }

    protected void onAbilityEvent(Message message) {

    }

    public void sendAbilityEvent(Message message) {
        findNavController().sendAbilityEvent(message);
    }

    private Toolbar toolbar;
    private CharSequence title;

    public Toolbar createDefaultToolbar() {
        if (this.viewParent.getChildCount() > 0) {
            if (this.viewParent.getChildAt(0) instanceof Toolbar) {
                return (Toolbar) this.viewParent.getChildAt(0);
            }
        }
        Toolbar toolbar = new Toolbar(getContext());
        TypedArray array = getActivity().getTheme().obtainStyledAttributes(new int[]{
                R.attr.colorPrimary,
                R.attr.colorOnPrimary,
                R.attr.actionBarSize,
        });
        int actionBarSize = (int) array.getDimension(2, ViewUtils.dp2px(getContext(), 56));
        toolbar.setBackgroundColor(array.getColor(0, 0));
        toolbar.setTitleTextColor(array.getColor(1, 0));
        this.viewParent.addView(toolbar, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, actionBarSize));
        setToolbar(toolbar);
        return toolbar;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        if (this.toolbar != null) {
            this.toolbar.setTitle(title);
        }
    }

    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    public CharSequence getTitle() {
        return title;
    }


    public void setResult(Bundle result) {

    }

    public void showDialog(Dialog dialog){

    }
}
