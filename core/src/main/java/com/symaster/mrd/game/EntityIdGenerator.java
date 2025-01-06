package com.symaster.mrd.game;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class EntityIdGenerator {

    private static long thatId = 0;

    public static synchronized long nextId() {
        return thatId++;
    }
}
