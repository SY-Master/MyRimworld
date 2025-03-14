package com.symaster.mrd.game.entity;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public enum NodeStatusEnum {

    FEED("吃东西"),
    THINK("思考"),
    MOVE("移动"),
    WAIT("等待"),
    SLEEP("睡觉"),

    ;

    private final String desc;

    NodeStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
