package com.symaster.mrd.util;

/**
 * @author yinmiao
 * @since 2025/1/3
 */
public class GeomUtil {

    /**
     * 计算两点之间距离
     */
    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


}
