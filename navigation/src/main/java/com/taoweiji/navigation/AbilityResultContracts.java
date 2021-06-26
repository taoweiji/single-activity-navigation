package com.taoweiji.navigation;

public class AbilityResultContracts {
    private AbilityResultCallback callback;

    public void registerForResult(AbilityResultCallback callback) {
        this.callback = callback;
    }
}
