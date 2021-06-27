package com.taoweiji.navigation.example.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.example.mvvm.MvvmAbility;

import org.jetbrains.annotations.NotNull;

public class MyDialog extends DialogFragment {
    Ability ability;

    public MyDialog(Ability ability) {
        this.ability = ability;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
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

        return dialog.create();
    }

}
