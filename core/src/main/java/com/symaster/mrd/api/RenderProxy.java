package com.symaster.mrd.api;

/**
 * 渲染代理
 *
 * @author yinmiao
 * @since 2025/1/2
 */
public interface RenderProxy {

    /**
     * 代理渲染
     *
     * @param delta 上一帧耗时 s
     */
    void render(float delta);
}
