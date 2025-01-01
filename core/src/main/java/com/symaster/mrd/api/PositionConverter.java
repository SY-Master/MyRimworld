package com.symaster.mrd.api;

import com.badlogic.gdx.math.Vector2;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public interface PositionConverter {

    void toWorld(Vector2 screen);

    void toScreen(Vector2 world);

}
