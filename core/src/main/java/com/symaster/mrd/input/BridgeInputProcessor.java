package com.symaster.mrd.input;

import com.badlogic.gdx.InputProcessor;

/**
 * @author yinmiao
 * @since 2025/1/2
 */
public interface BridgeInputProcessor extends InputProcessor {

    /**
     * @return 组
     */
    String group();

    /**
     * @return 层
     */
    int uiLayer();

    /**
     * @return 返回当前事件顺序
     */
    int uiSort();

    /**
     * @return 是否启用输入事件
     */
    boolean actionEnable();

}
