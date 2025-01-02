package com.symaster.mrd.input;

import com.badlogic.gdx.InputProcessor;

/**
 * @author yinmiao
 * @since 2025/1/2
 */
public interface BridgeInputProcessor extends InputProcessor {

    /**
     * @return 层
     */
    int layer();

    /**
     * @return 返回当前事件顺序
     */
    int sort();

}
