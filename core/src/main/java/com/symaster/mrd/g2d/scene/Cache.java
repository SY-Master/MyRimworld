package com.symaster.mrd.g2d.scene;

import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public final class Cache implements Serializable {
    private static final long serialVersionUID = 1L;

    public final List<Node> nodes = new LinkedList<>();
    /**
     * 暂存发生移动的组件
     */
    public final List<MoveNodeCache> moveNodes = new LinkedList<>();
    public Block cacheBlock;
}
