package com.taoweiji.navigation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import static android.view.View.NO_ID;

public abstract class Ability implements LifecycleOwner {
    private AbilityResultContracts abilityResultContracts;
    private LifecycleRegistry mLifecycleRegistry;
    private Context context;
    private Bundle arguments;
    private AbilityViewParent viewParent;
    private boolean createViewed;
    private final ToolbarAndStatusBarWrapper toolbarWrapper = new ToolbarAndStatusBarWrapper(this);
    private CharSequence title;

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
        if (arguments == null) {
            this.arguments = new Bundle();
        }
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
        toolbarWrapper.onResume();
        resumed = true;
    }

    protected abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    @CallSuper
    protected void onPause() {
        resumed = false;
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @CallSuper
    protected void onStop() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @CallSuper
    protected void onDestroy() {
        destroyed = true;
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    void performOnNewArguments(Bundle arguments) {
        setArguments(arguments);
        onNewArguments(getArguments());
    }

    protected void onNewArguments(Bundle arguments) {

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    public void startActivityForResult(Intent intent, ActivityResultCallback callback) {
        startActivityForResult(intent, 1, callback);
    }

    public void startActivityForResult(Intent intent, int requestCode, ActivityResultCallback callback) {
        startActivityForResult(intent, requestCode, null, callback);
    }

    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options, ActivityResultCallback callback) {
        StartActivityForResultFragment.startActivityForResult(intent, requestCode, options, getActivity(), callback);
    }

    public Context getContext() {
        return context;
    }

    public final FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }


    /**
     * 预创建
     */
    public void prepareCreate(Context context, Bundle arguments) {
        this.setContext(context);
        this.setArguments(arguments);
        performCreateViewParent(null);
        performCreateView(null);
    }


    void performCreateView(@Nullable Bundle savedInstanceState) {
        if (createViewed) {
            return;
        }
        createViewed = true;
        Navigation.onPreCreate(this, savedInstanceState);
        onCreate(null);
        Navigation.onCreate(this, savedInstanceState);
        Navigation.onPreCreateView(this, savedInstanceState);
        View view = onCreateView(LayoutInflater.from(context), viewParent, savedInstanceState);
        Navigation.onCreateView(this, savedInstanceState);
        if (view != null) {
            viewParent.addContent(view);
        }
        Navigation.onPreViewCreated(this);
        this.onViewCreated(view, null);
        Navigation.onViewCreated(this);
    }

    AbilityViewParent performCreateViewParent(NavController navController) {
        if (viewParent == null) {
            viewParent = new AbilityViewParent(context, navController, this);
            Toolbar toolbar = createDefaultToolbar();
            if (toolbar != null) {
                viewParent.addToolbar(toolbar);
                setToolbar(viewParent.getToolbar());
            }
            setToolbarBackgroundColor(Color.WHITE);
        } else {
            viewParent.navController = navController;
        }
        return viewParent;
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

    boolean finished = false;

    @CallSuper
    public void finish() {
        if (finished) {
            return;
        }
        if (abilityResultContracts != null) {
            abilityResultContracts.setResultData(resultData);
        }
        findNavController().finish(this);
        finished = true;
    }

    public boolean isFinishing() {
        return finished;
    }


    public void sendAbilityEvent(Message message) {
        findNavController().sendAbilityEvent(message);
    }


    public void setTitle(CharSequence title) {
        toolbarWrapper.setTitle(title);
        this.title = title;
    }

    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    public CharSequence getTitle() {
        return title;
    }


    Bundle resultData = new Bundle();

    public void setResult(Bundle result) {
        this.resultData = result;
    }


    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == NO_ID) {
            return null;
        }
        return getViewParent().findViewById(id);
    }

    private boolean resumed = false;

    public boolean isResumed() {
        return resumed;
    }

    public void onBackPressed() {
        this.finish();
    }

    void setContext(Context context) {
        this.context = context;
    }

    void setAbilityResultContracts(AbilityResultContracts abilityResultContracts) {
        this.abilityResultContracts = abilityResultContracts;
    }

    void performOnResume() {
        destroyed = false;
        if (!isResumed()) {
            Navigation.onPreStart(this);
            onStart();
            Navigation.onStart(this);
            Navigation.onPreResume(this);
            onResume();
            Navigation.onResume(this);
        }
    }

    void performOnPause() {
        if (isResumed()) {
            Navigation.onPrePause(this);
            onPause();
            Navigation.onPause(this);
            Navigation.onPreStop(this);
            onStop();
            Navigation.onStop(this);
        }
    }

    private boolean destroyed = false;

    public boolean isDestroyed() {
        return destroyed;
    }

    void performOnDestroy() {
        if (!destroyed) {
            Navigation.onPreDestroy(this);
            onDestroy();
            Navigation.onDestroy(this);
        }
    }

    protected void onEvent(Message message) {

    }

    public Toolbar getToolbar() {
        return toolbarWrapper.getToolbar();
    }

    public void setToolbar(Toolbar toolbar) {
        toolbarWrapper.setToolbar(toolbar);
        invalidateOptionsMenu();
    }


    public enum StatusBarTextStyle {
        BLACK, WHITE, HIDE
    }

    public void setStatusBarTextStyle(StatusBarTextStyle style) {
        toolbarWrapper.setStatusBarTextStyle(style);
    }

    /**
     * @param marginTop 如果是-1，就是默认在Toolbar的下面，如果是 0就是到顶实现沉浸模式
     */
    public void setContentViewMarginTop(int marginTop) {
        getViewParent().setContentViewMarginTop(marginTop);
    }

    /**
     * 会自动修改状态栏颜色和按钮颜色，如果不需要请使用 getToolbar().setBackgroundColor()
     */
    public void setToolbarBackgroundColor(int color) {
        toolbarWrapper.setToolbarBackgroundColor(color);
    }

    public void setToolbarHeight(int height) {
        setToolbarHeight(height, true);
    }

    /**
     * @param height            状态栏高度
     * @param fitsSystemWindows 自动留出到顶部的高度
     */
    public void setToolbarHeight(int height, boolean fitsSystemWindows) {
        Toolbar toolbar = toolbarWrapper.getToolbar();
        if (toolbar == null) return;
        if (fitsSystemWindows) {
            int statusBarHeight = ViewUtils.getStatusBarHeight(getContext());
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            height += statusBarHeight;
        }
        toolbar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
    }

    public void setDefaultDisplayHomeAsUpEnabled(boolean enabled) {
        toolbarWrapper.setDefaultDisplayHomeAsUpEnabled(enabled);
    }

    protected Toolbar createDefaultToolbar() {
        Toolbar toolbar = new Toolbar(getContext());
        toolbar.setElevation(ViewUtils.dp2px(getContext(), 2));

        int statusBarHeight = ViewUtils.getStatusBarHeight(getContext());
        toolbar.setPadding(0, statusBarHeight, 0, 0);
        int height = ViewUtils.dp2px(getContext(), 56) + statusBarHeight;
        toolbar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
        return toolbar;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

    }

    public void invalidateOptionsMenu() {
        if (getToolbar() != null) {
            getToolbar().getMenu().clear();
            onCreateOptionsMenu(getToolbar().getMenu(), new MenuInflater(getContext()));
        }
    }


    public void showDialog(Dialog dialog) {
        // TODO
    }
}
