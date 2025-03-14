package com.symaster.mrd.game.core.ai;

/**
 * @author yinmiao
 * @since 2025/3/7
 */
public enum AiRequestType {
    // 未开始、请求中、结束
    NOT_STARTED(0),
    /**
     * 请求中
     */
    REQUESTING(1),
    /**
     * 完成
     */
    FINISH(2);

    private final int value;

    AiRequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
