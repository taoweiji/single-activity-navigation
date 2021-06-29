package com.taoweiji.navigation.example;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.NavController;

import org.jetbrains.annotations.NotNull;

public class SimpleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView hello = new TextView(getContext());
        hello.setGravity(Gravity.CENTER);
        hello.setText("Fragment");
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController.findNavController(v).navigate("user");
            }
        });
        return hello;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Ability ability = NavController.findAbility(this);
        ability.createDefaultToolbar();
        ability.setTitle("直接跳转 Fragment");
    }

    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SimpleFragment", "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("SimpleFragment", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("SimpleFragment", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("SimpleFragment", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("SimpleFragment", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SimpleFragment", "onDestroy");
    }
}
