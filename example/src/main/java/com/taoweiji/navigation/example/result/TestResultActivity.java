package com.taoweiji.navigation.example.result;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(1, new Intent().putExtra("msg", "TestResultActivity"));
        setTitle("设置返回值 msg = TestResultActivity");
    }
}
