package com.symaster.mrd;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class SystemConfig {

    /**
     * 本地化文件
     */
    public static String TEXT_PATH = "language/ch.txt";
    /**
     * 使用字体
     */
    public static String FONT_PATH = "fonts/SweiDelLunaSansCJKsc-Black.ttf";
    /**
     * 字体大小
     */
    public static int[] FONT_SIZES = new int[]{14, 16, 18, 20};
    /**
     * 区块大小（米）默认10米
     */
    public static float BLOCK_SIZE = 10f;
    /**
     * 一个区块的地板数量
     */
    public static int MAP_NUMBER = 10;
    /**
     * 时间缩放
     */
    public static float TIME_SCALE = 1f;
    /**
     * GUI缩放因子
     */
    public static float GUI_SCALE = 1f;
    /**
     * 激活伙伴周围的区块
     */
    public static int PARTNER_ACTIVE_SIZE = 3;

}
