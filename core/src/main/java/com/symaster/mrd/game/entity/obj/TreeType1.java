package com.symaster.mrd.game.entity.obj;

import com.badlogic.gdx.assets.AssetManager;
import com.symaster.mrd.util.UnitUtil;

/**
 * @author yinmiao
 * @since 2025/1/9
 */
public class TreeType1 extends Tree {

    public TreeType1(AssetManager assetManager) {
        super(assetManager, 24, 14, 114, 140, UnitUtil.ofM(3));
    }
}
