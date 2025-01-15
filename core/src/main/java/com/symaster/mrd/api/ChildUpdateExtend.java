package com.symaster.mrd.api;

import com.symaster.mrd.g2d.Node;

/**
 * {@link Node} 子节点操作事件，当{@link Node}添加或删除子节点时触发相应函数
 *
 * @author yinmiao
 * @since 2024/12/26
 */
public interface ChildUpdateExtend {

    /**
     * 添加之后
     */
    void afterAdd(Node parent, Node child);

    /**
     * 删除之后
     */
    void afterRemove(Node parent, Node child);

}
