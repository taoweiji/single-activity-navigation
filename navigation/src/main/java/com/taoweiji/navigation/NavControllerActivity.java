package com.taoweiji.navigation;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public abstract class NavControllerActivity extends AppCompatActivity {
    private NavController navController;

    public abstract NavController.Builder createNavControllerBuilder();

    protected NavController getNavController() {
        return navController;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarHelper.openImmerseStyle(this);
        super.onCreate(savedInstanceState);
        FrameLayout layout = new FrameLayout(this);
        setContentView(layout);
        navController = createNavControllerBuilder().create(layout);
    }

    @Override
    public void onBackPressed() {
        if (getNavController().canBack()) {
            getNavController().dispatcherOnBackPressed();
            return;
        }
        super.onBackPressed();
    }
}
