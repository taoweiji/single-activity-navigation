package com.taoweiji.navigation.example.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.example.mvvm.MvvmAbility;

import org.jetbrains.annotations.NotNull;

public class DialogAbility2 extends Fragment {
    @NonNull
    @NotNull
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        Button button1 = new Button(getContext());
        button1.setText("普通对话框（不推荐）");
        layout.addView(button1);
        Button button2 = new Button(getContext());
        button2.setText("Fragment 对话框（推荐）");
        layout.addView(button2);
        Ability ability = NavController.findAbility(this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("测试Dialog");
                dialog.setMessage("DialogFragment");
                Button button = new Button(getContext());
                button.setText("跳转");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ability.findNavController().navigate(new MvvmAbility());
                    }
                });
                dialog.setView(button);
                dialog.setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ability.findNavController().navigate(new MvvmAbility());
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialogFragment = new MyDialog(NavController.findAbility(DialogAbility2.this));
                dialogFragment.show(getChildFragmentManager(), "hello");
            }
        });
        return layout;
    }
}

