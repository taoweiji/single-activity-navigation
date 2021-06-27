package com.taoweiji.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public interface ActivityResultCallback {

    /**
     * Called when result is available
     */
   void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
}
