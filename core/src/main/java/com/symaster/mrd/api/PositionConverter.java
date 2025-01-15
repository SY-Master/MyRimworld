package com.symaster.mrd.api;

import com.badlogic.gdx.math.Vector2;

/**
 * 坐标转换器，屏幕坐标与世界坐标互相转换
 *
 * @author yinmiao
 * @since 2025/1/1
 */
public interface PositionConverter {

    /**
     * 屏幕坐标转世界坐标
     *
     * @param screen 屏幕坐标
     */
    void toWorld(Vector2 screen);

    /**
     * 世界坐标转屏幕坐标
     *
     * @param world 世界坐标
     */
    void toScreen(Vector2 world);

}
