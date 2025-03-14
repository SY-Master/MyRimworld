package com.symaster.mrd.game.entity.obj;

import com.badlogic.gdx.assets.AssetManager;
import com.symaster.mrd.util.UnitUtil;

/**
 * @author yinmiao
 * @since 2025/1/9
 */
public class BushesType1 extends Bushes {

    public BushesType1(AssetManager assetManager) {
        super(assetManager, 38, 198, 24, 24, UnitUtil.ofM(1f));
    }

}
