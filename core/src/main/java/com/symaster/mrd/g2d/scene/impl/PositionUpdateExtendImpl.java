package com.symaster.mrd.g2d.scene.impl;

import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.MoveNodeCache;
import com.symaster.mrd.g2d.scene.Scene;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class PositionUpdateExtendImpl implements PositionUpdateExtend {

    private final Scene scene;

    public PositionUpdateExtendImpl(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void afterUpdate(Node node, float oldX, float oldY, float newX, float newY) {
        if (node.getParent() != null) {
            return;
        }

        MoveNodeCache moveNodeCache = new MoveNodeCache();
        moveNodeCache.node = node;
        moveNodeCache.oldX = oldX;
        moveNodeCache.oldY = oldY;
        moveNodeCache.newX = newX;
        moveNodeCache.newY = newY;

        scene.getRenderCache().moveNodes.add(moveNodeCache);
    }
}
