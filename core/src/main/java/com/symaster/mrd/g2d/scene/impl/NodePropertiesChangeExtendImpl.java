package com.symaster.mrd.g2d.scene.impl;

import com.symaster.mrd.api.NodePropertiesChangeExtend;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class NodePropertiesChangeExtendImpl implements NodePropertiesChangeExtend {

    private final Scene scene;

    public NodePropertiesChangeExtendImpl(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void activityBlockSizeAfterUpdate(Node node, int oldSize, int newSize) {
        if (node.getParent() != null) {
            return;
        }
        scene.blockUpdate(node, newSize);
    }
}
