package com.symaster.mrd.api;

import com.badlogic.gdx.assets.AssetManager;

/**
 * @author yinmiao
 * @since 2025/3/22
 */
public class AssetManagerProxy {

    private final AssetManager assetManager;

    public AssetManagerProxy(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
