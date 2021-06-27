package com.taoweiji.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class StartActivityForResultFragment extends Fragment {
    Intent intent;
    int requestCode;
    @Nullable
    Bundle options;
    ActivityResultCallback callback;

    public StartActivityForResultFragment(Intent intent, int requestCode, @Nullable Bundle options, ActivityResultCallback callback) {
        this.intent = intent;
        this.requestCode = requestCode;
        this.options = options;
        this.callback = callback;
    }

    public static void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options, FragmentActivity activity, ActivityResultCallback callback) {
        activity.getSupportFragmentManager().beginTransaction().add(new StartActivityForResultFragment(intent, requestCode, options, callback), "StartActivity").commitNow();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(StartActivityForResultFragment.this).commit();
        }
    }
}
