package com.symaster.mrd.api;

import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2024/12/24
 */
public interface PositionUpdateExtend {

    /**
     * 更新之后
     */
    void afterUpdate(Node node, float oldX, float oldY, float newX, float newY);

}
