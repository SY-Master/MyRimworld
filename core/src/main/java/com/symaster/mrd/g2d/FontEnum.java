package com.symaster.mrd.g2d;

/**
 * @author yinmiao
 * @since 2025/6/12
 */
public enum FontEnum {

    BASE_FONT("baseFont"),

    ;

    private final String key;


    FontEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
