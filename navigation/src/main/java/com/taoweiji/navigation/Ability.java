package com.taoweiji.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import static android.view.View.NO_ID;

import java.util.HashMap;
import java.util.Map;

public abstract class Ability implements LifecycleOwner, ViewModelStoreOwner {
    private AbilityResultContracts abilityResultContracts;
    private LifecycleRegistry mLifecycleRegistry;
    private Context context;
    private Bundle arguments;
    private AbilityViewParent viewParent;
    private boolean createViewed;
    private final ToolbarAndStatusBarWrapper toolbarWrapper = new ToolbarAndStatusBarWrapper(this);
    private CharSequence title;
    int overrideEnterAnim = -1;
    int overrideExitAnim = -1;
    NavOptions navOptions;
    private String abilityId;
    // attribute
    private final Map<String, Object> tags = new HashMap<>();
    ViewModelStore viewModelStore;

    public Ability() {
        initLifecycle();
        viewModelStore = new ViewModelStore();
    }

    @NonNull
    public Bundle getArguments() {
        if (arguments == null) {
            arguments = new Bundle();
        }
        return arguments;
    }

    public String getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(String abilityId) {
        this.abilityId = abilityId;
    }

    public void setTag(String name, Object object) {
        tags.put(name, object);
    }

    public Object getTag(String name) {
        return tags.get(name);
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
        SoftKeyboardHelper.hideSoftKeyboard(getActivity());
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
        if (context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        } else {
            return null;
        }
    }


    /**
     * ?????????
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
        if (view != null && view != viewParent) {
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

    public AbilityViewParent getDecorView() {
        return viewParent;
    }

    boolean finished = false;

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
        if (toolbarWrapper.getToolbar() != null) {
            return toolbarWrapper.getToolbar().getTitle();
        }
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
        return getDecorView().findViewById(id);
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
            createViewed = false;
//            viewParent = null;
//            arguments = null;
            // TODO ????????????
            onDestroyView();
            onDestroy();
            Navigation.onDestroy(this);
        }
    }

    protected void onEvent(Message message) {

    }

    public void setEvent(Message message) {
        onEvent(message);
    }

    public Toolbar getToolbar() {
        return toolbarWrapper.getToolbar();
    }

    public void setToolbar(Toolbar toolbar) {
        toolbarWrapper.setToolbar(toolbar);
        invalidateOptionsMenu();
    }

    /**
     * @param style {@link StatusBarHelper#STYLE_WHITE,StatusBarHelper#STYLE_BLACK,StatusBarHelper#STYLE_FULLSCREEN,StatusBarHelper#STYLE_FULLSCREEN_WITHOUT_CUTOUT}
     */
    public void setStatusBarStyle(@StatusBarHelper.Style int style) {
        toolbarWrapper.setStatusBarStyle(style);
    }

    /**
     * @param marginTop ?????????-1??????????????????Toolbar????????????????????? 0??????????????????????????????
     */
    public void setContentViewMarginTop(int marginTop) {
        getDecorView().setContentViewMarginTop(marginTop);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????? getToolbar().setBackgroundColor()
     */
    public void setToolbarBackgroundColor(int color) {
        toolbarWrapper.setToolbarBackgroundColor(color);
    }

    public void setToolbarHeight(int height) {
        setToolbarHeight(height, true);
    }

    /**
     * @param height            ???????????????
     * @param fitsSystemWindows ??????????????????????????????
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

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        this.overrideEnterAnim = enterAnim;
        this.overrideExitAnim = exitAnim;
    }

    public final void runOnUiThread(Runnable action) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(action);
        } else {
            action.run();
        }
    }

    @CallSuper
    public void onDestroyView() {

    }

    @CallSuper
    protected void onAttach(@NonNull Context context) {

    }

    @CallSuper
    public void onDetach() {

    }

    public FrameLayout getCoverView() {
        return getDecorView().getCoverView();
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }
}
