package com.symaster.mrd.util;

/**
 * @author yinmiao
 * @since 2024/12/25
 */
public class UnitUtil {

    /**
     * 一米等于多少空间单位
     */
    private static final float SIZE = 100;

    /**
     * 将米转换成游戏空间单位
     *
     * @param m 值
     */
    public static float ofM(float m) {
        return m * SIZE;
    }

    /**
     * 将游戏空间单位转换成米
     *
     * @param scene 空间单位
     * @return 米
     */
    public static float ofScene(float scene) {
        return scene / SIZE;
    }

}
