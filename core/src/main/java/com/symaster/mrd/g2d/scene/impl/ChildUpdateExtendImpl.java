package com.symaster.mrd.g2d.scene.impl;

import com.symaster.mrd.api.ChildUpdateExtend;
import com.symaster.mrd.g2d.OrthographicCameraNode;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class ChildUpdateExtendImpl implements ChildUpdateExtend {
    private final Scene scene;

    public ChildUpdateExtendImpl(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void afterAdd(Node parent, Node child) {
        scene.addExtendEvent(child);

        if (child instanceof OrthographicCameraNode) {
            scene.getCameraNodes().add((OrthographicCameraNode) child);
        }

        child.onScene(scene);
        for (Node node : child) {
            node.onScene(scene);
        }
    }

    @Override
    public void afterRemove(Node parent, Node child) {
        scene.removeExtendEvent(child);

        if (child instanceof OrthographicCameraNode) {
            scene.getCameraNodes().remove(child);
        }

        child.extScene(scene);
        for (Node node : child) {
            node.extScene(scene);
        }
    }
}
