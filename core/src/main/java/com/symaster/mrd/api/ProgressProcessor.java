package com.symaster.mrd.api;

/**
 * 进度条更新事件
 *
 * @author yinmiao
 * @since 2024/12/31
 */
public interface ProgressProcessor {

    /**
     * 进度条更新
     *
     * @param progress 新进度 [0-1]
     */
    void update(float progress);

}
