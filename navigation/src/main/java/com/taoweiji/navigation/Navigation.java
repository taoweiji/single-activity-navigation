package com.taoweiji.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Navigation {
    private static final List<AbilityLifecycleCallbacks> callbacks = new ArrayList<>();

    public static void registerAbilityLifecycleCallbacks(AbilityLifecycleCallbacks callback) {
        synchronized (callbacks) {
            callbacks.add(callback);
        }
    }

    public static void unregisterAbilityLifecycleCallbacks(AbilityLifecycleCallbacks callback) {
        synchronized (callbacks) {
            callbacks.remove(callback);
        }
    }

    static void onPause(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPaused(ability);
            }
        }
    }

    static void onResume(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityResumed(ability);
            }
        }
    }

    static void onDestroy(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityDestroyed(ability);
            }
        }
    }

    static void onCreate(Ability ability, Bundle savedInstanceState) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityCreated(ability, savedInstanceState);
            }
        }
    }

    static void onStop(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityStopped(ability);
            }
        }
    }

    static void onStart(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityStarted(ability);
            }
        }
    }

    static void onViewCreated(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityViewCreated(ability);
            }
        }
    }

    static void onPreViewCreated(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPreViewCreated(ability);
            }
        }
    }

    static void onPreCreate(Ability ability, Bundle savedInstanceState) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPreCreated(ability, savedInstanceState);
            }
        }
    }

    static void onPreStart(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPreStarted(ability);
            }
        }
    }

    static void onPreResume(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPreResumed(ability);
            }
        }
    }

    static void onPrePause(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPrePaused(ability);
            }
        }
    }

    static void onPreStop(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPreStopped(ability);
            }
        }
    }

    static void onPreDestroy(Ability ability) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPreDestroyed(ability);
            }
        }
    }

    public static void onPreCreateView(Ability ability, Bundle savedInstanceState) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityPreCreateViewed(ability);
            }
        }
    }

    public static void onCreateView(Ability ability, Bundle savedInstanceState) {
        synchronized (callbacks) {
            for (AbilityLifecycleCallbacks it : callbacks) {
                it.onAbilityCreateViewed(ability);
            }
        }
    }


    public interface AbilityLifecycleCallbacks {

        /**
         * Called as the first step of the Ability being created. This is always called before
         * {@link Ability#onCreate}.
         */
        default void onAbilityPreCreated(@NonNull Ability ability, @Nullable Bundle savedInstanceState) {
        }

        /**
         * Called when the Ability calls {@link Ability#onCreate super.onCreate()}.
         */
        void onAbilityCreated(@NonNull Ability ability, @Nullable Bundle savedInstanceState);

        /**
         * Called as the first step of the Ability being started. This is always called before
         * {@link Ability#onStart}.
         */
        default void onAbilityPreStarted(@NonNull Ability ability) {
        }

        /**
         * Called when the Ability calls {@link Ability#onStart super.onStart()}.
         */
        void onAbilityStarted(@NonNull Ability ability);


        /**
         * Called as the first step of the Ability being resumed. This is always called before
         * {@link Ability#onResume}.
         */
        default void onAbilityPreResumed(@NonNull Ability ability) {
        }

        /**
         * Called when the Ability calls {@link Ability#onResume super.onResume()}.
         */
        void onAbilityResumed(@NonNull Ability ability);


        /**
         * Called as the first step of the Ability being paused. This is always called before
         * {@link Ability#onPause}.
         */
        default void onAbilityPrePaused(@NonNull Ability ability) {
        }

        /**
         * Called when the Ability calls {@link Ability#onPause super.onPause()}.
         */
        void onAbilityPaused(@NonNull Ability ability);

        /**
         * Called as the first step of the Ability being stopped. This is always called before
         * {@link Ability#onStop}.
         */
        default void onAbilityPreStopped(@NonNull Ability ability) {
        }

        /**
         * Called when the Ability calls {@link Ability#onStop super.onStop()}.
         */
        void onAbilityStopped(@NonNull Ability ability);

        /**
         * Called as the first step of the Ability being destroyed. This is always called before
         * {@link Ability#onDestroy}.
         */
        default void onAbilityPreDestroyed(@NonNull Ability ability) {
        }

        /**
         * Called when the Ability calls {@link Ability#onDestroy super.onDestroy()}.
         */
        void onAbilityDestroyed(@NonNull Ability ability);

        void onAbilityViewCreated(@NonNull Ability ability);

        default void onAbilityPreViewCreated(@NonNull Ability ability) {

        }

        default void onAbilityPreCreateViewed(@NonNull Ability ability) {

        }

        void onAbilityCreateViewed(@NonNull Ability ability);
    }
}
