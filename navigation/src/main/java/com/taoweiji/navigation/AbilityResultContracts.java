package com.taoweiji.navigation;

import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AbilityResultContracts {
    private WeakReference<Ability> starter;

    private AbilityResultCallback callback;
    private final List<Listener> listeners = new ArrayList<>();

     AbilityResultContracts() {
//        this.starter = new WeakReference<>(starter);
    }

    public void registerForResult(AbilityResultCallback callback) {
        this.callback = callback;
    }

    void setResultData(Bundle resultData) {
        if (callback != null) {
            callback.onAbilityResult(resultData);
        }
    }

    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    void setNavigationEnd() {
        for (Listener it : listeners) {
            it.onNavigateEnd();
        }
    }

    public interface Listener {
        void onNavigateEnd();
    }
}
