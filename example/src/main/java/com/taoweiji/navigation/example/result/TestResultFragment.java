package com.taoweiji.navigation.example.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.NavController;

import org.jetbrains.annotations.NotNull;

public class TestResultFragment extends Fragment {
    @Nullable

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        return new View(getContext());
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Ability ability = NavController.findAbility(this);
        ability.setTitle("设置返回值 msg = TestResultFragment");
        ability.setResult(new BundleBuilder().put("msg", "TestResultFragment").build());
    }
}
