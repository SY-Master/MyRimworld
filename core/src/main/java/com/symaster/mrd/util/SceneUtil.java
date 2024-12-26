package com.symaster.mrd.util;

import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2024/12/26
 */
public class SceneUtil {

    public static Node getTopParent(Node child) {
        if (child.getParent() == null) {
            return child;
        }

        return getTopParent(child.getParent());
    }
}
