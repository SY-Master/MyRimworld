package com.symaster.mrd.api;

import com.symaster.mrd.g2d.Node;

/**
 * {@link Node} 属性更新事件
 *
 * @author yinmiao
 * @since 2024/12/24
 */
public interface NodePropertiesChangeExtend {

    /**
     * "activityBlockSize"更新之后
     */
    void activityBlockSizeAfterUpdate(Node node, int oldSize, int newSize);

}
