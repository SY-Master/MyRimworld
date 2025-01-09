package com.symaster.mrd.game.entity.obj;

import com.badlogic.gdx.assets.AssetManager;
import com.symaster.mrd.util.UnitUtil;

/**
 * @author yinmiao
 * @since 2025/1/9
 */
public class TreeType2 extends Tree {

    public TreeType2(AssetManager assetManager) {
        super(assetManager, 160, 16, 98, 138, UnitUtil.ofM(2.8f));
    }
}
