package com.symaster.mrd.game.entity;

/**
 * 种族
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public enum Race {

    Human("人类", com.symaster.mrd.game.entity.Human.class),

    ;

    private final String name;
    private final Class<?> clazz;

    Race(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
