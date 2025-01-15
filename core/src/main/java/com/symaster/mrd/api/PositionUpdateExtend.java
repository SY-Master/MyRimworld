package com.symaster.mrd.api;

import com.symaster.mrd.g2d.Node;

/**
 * {@link Node} 坐标更新事件
 *
 * @author yinmiao
 * @since 2024/12/24
 */
public interface PositionUpdateExtend {

    /**
     * 更新之后
     */
    void afterUpdate(Node node, float oldX, float oldY, float newX, float newY);

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
    boolean beforeUpdate(Node node, float oldX, float oldY, float newX, float newY);

}
