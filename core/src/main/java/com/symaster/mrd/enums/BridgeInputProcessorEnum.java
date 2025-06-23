package com.symaster.mrd.enums;

/**
 * @author yinmiao
 * @since 2025/6/22
 */
public enum BridgeInputProcessorEnum {
    PAGE("PAGE"),
    GAME_UI("GAME_UI"),
    SCENE("SCENE"),

    ;

    private final String code;

    BridgeInputProcessorEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
