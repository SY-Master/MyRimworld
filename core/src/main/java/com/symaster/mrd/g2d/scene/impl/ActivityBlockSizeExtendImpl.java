package com.symaster.mrd.g2d.scene.impl;

import com.symaster.mrd.api.ActivityBlockSizeExtend;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class ActivityBlockSizeExtendImpl implements ActivityBlockSizeExtend {

    private final Scene scene;

    public ActivityBlockSizeExtendImpl(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void afterUpdate(Node node, int oldSize, int newSize) {
        if (node.getParent() != null) {
            return;
        }
        scene.blockUpdate(node, newSize);
    }
}
