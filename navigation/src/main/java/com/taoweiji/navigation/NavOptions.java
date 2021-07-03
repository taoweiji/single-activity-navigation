package com.taoweiji.navigation;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;


public class NavOptions {
    public static NavOptions UP_DOWN = new NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_up)
            .setExitAnim(0)
            .setPopEnterAnim(0)
            .setPopExitAnim(R.anim.slide_out_down)
            .build();

    public static NavOptions LEFT_RIGHT = new NavOptions.Builder()
            .setEnterAnim(R.anim.ability_left_right_enter_anim)
            .setExitAnim(R.anim.ability_left_right_exit_anim)
            .setPopEnterAnim(R.anim.ability_left_right_pop_enter_anim)
            .setPopExitAnim(R.anim.ability_left_right_pop_exit_anim)
            .build();

    public static NavOptions DEFAULT = new NavOptions.Builder()
            .setEnterAnim(R.anim.activity_open_enter)
            .setExitAnim(R.anim.activity_open_exit)
            .setPopEnterAnim(R.anim.activity_close_enter)
            .setPopExitAnim(R.anim.activity_close_exit)
            .build();

    public static NavOptions NONE = new NavOptions.Builder()
            .setEnterAnim(0)
            .setExitAnim(0)
            .setPopEnterAnim(0)
            .setPopExitAnim(0)
            .build();


//    public static NavOptions getSystem(Context context) {
//        if (SYSTEM != null) return SYSTEM;
//        int activity_open_enter = context.getResources().getIdentifier("@android:anim/activity_open_enter", null, context.getPackageName());
//        int activity_open_exit = context.getResources().getIdentifier("@android:anim/activity_open_exit", null, context.getPackageName());
//        int activity_close_enter = context.getResources().getIdentifier("@android:anim/activity_close_enter", null, context.getPackageName());
//        int activity_close_exit = context.getResources().getIdentifier("@android:anim/activity_close_exit", null, context.getPackageName());
//        if (activity_open_enter == 0 || activity_open_exit == 0 || activity_close_enter == 0 || activity_close_exit == 0) {
//            return DEFAULT;
//        }
//        SYSTEM = new NavOptions.Builder().setEnterAnim(activity_open_enter)
//                .setExitAnim(activity_open_exit)
//                .setPopEnterAnim(activity_close_enter)
//                .setPopExitAnim(activity_close_exit).build();
//        return SYSTEM;
//    }


    @AnimRes
    @AnimatorRes
    private final int mEnterAnim;
    @AnimRes
    @AnimatorRes
    private final int mExitAnim;
    @AnimRes
    @AnimatorRes
    private final int mPopEnterAnim;
    @AnimRes
    @AnimatorRes
    private final int mPopExitAnim;

    NavOptions(
            @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim,
            @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        mEnterAnim = enterAnim;
        mExitAnim = exitAnim;
        mPopEnterAnim = popEnterAnim;
        mPopExitAnim = popExitAnim;
    }


    /**
     * The custom enter Animation/Animator that should be run.
     *
     * @return the resource id of a Animation or Animator or -1 if none.
     */
    @AnimRes
    @AnimatorRes
    public int getEnterAnim() {
        return mEnterAnim;
    }

    /**
     * The custom exit Animation/Animator that should be run.
     *
     * @return the resource id of a Animation or Animator or -1 if none.
     */
    @AnimRes
    @AnimatorRes
    public int getExitAnim() {
        return mExitAnim;
    }

    /**
     * The custom enter Animation/Animator that should be run when this destination is
     * popped from the back stack.
     *
     * @return the resource id of a Animation or Animator or -1 if none.
     */
    @AnimRes
    @AnimatorRes
    public int getPopEnterAnim() {
        return mPopEnterAnim;
    }

    /**
     * The custom exit Animation/Animator that should be run when this destination is
     * popped from the back stack.
     *
     * @return the resource id of a Animation or Animator or -1 if none.
     */
    @AnimRes
    @AnimatorRes
    public int getPopExitAnim() {
        return mPopExitAnim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NavOptions that = (NavOptions) o;
        return
                mEnterAnim == that.mEnterAnim
                        && mExitAnim == that.mExitAnim
                        && mPopEnterAnim == that.mPopEnterAnim
                        && mPopExitAnim == that.mPopExitAnim;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + getEnterAnim();
        result = 31 * result + getExitAnim();
        result = 31 * result + getPopEnterAnim();
        result = 31 * result + getPopExitAnim();
        return result;
    }

    /**
     * Builder for constructing new instances of NavOptions.
     */
    public static final class Builder {
        @AnimRes
        @AnimatorRes
        int mEnterAnim = -1;
        @AnimRes
        @AnimatorRes
        int mExitAnim = -1;
        @AnimRes
        @AnimatorRes
        int mPopEnterAnim = -1;
        @AnimRes
        @AnimatorRes
        int mPopExitAnim = -1;

        public Builder() {
        }

        /**
         * Sets a custom Animation or Animator resource for the enter animation.
         *
         * <p>Note: Animator resources are not supported for navigating to a new Activity</p>
         *
         * @param enterAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getEnterAnim()
         */
        @NonNull
        public Builder setEnterAnim(@AnimRes @AnimatorRes int enterAnim) {
            mEnterAnim = enterAnim;
            return this;
        }

        /**
         * Sets a custom Animation or Animator resource for the exit animation.
         *
         * <p>Note: Animator resources are not supported for navigating to a new Activity</p>
         *
         * @param exitAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getExitAnim()
         */
        @NonNull
        public Builder setExitAnim(@AnimRes @AnimatorRes int exitAnim) {
            mExitAnim = exitAnim;
            return this;
        }

        /**
         * Sets a custom Animation or Animator resource for the enter animation
         * when popping off the back stack.
         *
         * <p>Note: Animator resources are not supported for navigating to a new Activity</p>
         *
         * @param popEnterAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getPopEnterAnim()
         */
        @NonNull
        public Builder setPopEnterAnim(@AnimRes @AnimatorRes int popEnterAnim) {
            mPopEnterAnim = popEnterAnim;
            return this;
        }

        /**
         * Sets a custom Animation or Animator resource for the exit animation
         * when popping off the back stack.
         *
         * <p>Note: Animator resources are not supported for navigating to a new Activity</p>
         *
         * @param popExitAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getPopExitAnim()
         */
        @NonNull
        public Builder setPopExitAnim(@AnimRes @AnimatorRes int popExitAnim) {
            mPopExitAnim = popExitAnim;
            return this;
        }

        /**
         * @return a constructed NavOptions
         */
        @NonNull
        public NavOptions build() {
            return new NavOptions(mEnterAnim, mExitAnim, mPopEnterAnim, mPopExitAnim);
        }
    }
}
