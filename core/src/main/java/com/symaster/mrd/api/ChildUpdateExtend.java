package com.symaster.mrd.api;

import com.symaster.mrd.g2d.Node;

/**
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
