package com.symaster.mrd.g2d.scene.impl;

import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;

import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/28
 */
public interface BlockMapGenerateProcessor {
    Set<Node> generate(Scene scene, Block take);
}
