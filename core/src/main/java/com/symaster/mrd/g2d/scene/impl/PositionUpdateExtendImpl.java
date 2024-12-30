package com.symaster.mrd.g2d.scene.impl;

import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.g2d.Block;
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

    /**
     * 更新之前
     *
     * @param node 目标
     * @param oldX 原x
     * @param oldY 原y
     * @param newX 新x
     * @param newY 新y
     * @return 是否继续移动
     */
    @Override
    public boolean beforeUpdate(Node node, float oldX, float oldY, float newX, float newY) {
        if (!node.isLimit2activityBlock()) {
            return true;
        }

        // 将移动限制在激活区块中
        if (inActivityBlock(newX, newY, node.getWidth(), node.getHeight())) {
            return true;
        }

        return !inActivityBlock(oldX, oldY, node.getWidth(), node.getHeight());
    }

    private boolean inActivityBlock(float x, float y, float width, float height) {

        for (Block activeBlock : scene.getActiveBlocks()) {
            float x1 = activeBlock.toX1(scene);
            float y1 = activeBlock.toY1(scene);
            float x2 = activeBlock.toX2(scene);
            float y2 = activeBlock.toY2(scene);

            if (x >= x1 && x + width < x2 && y >= y1 && y + height < y2) {
                return true;
            }
        }

        return false;
    }
}
