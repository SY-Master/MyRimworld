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
    public static float BLOCK_SIZE = 20f;
    /**
     * 一个区块的地板数量
     */
    public static int MAP_NUMBER = 40;
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
    /**
     * 游戏时间设定 - 一天多少个小时
     */
    public static int TIME_DAY_HOUR = 24;
    /**
     * 游戏时间设定 - 一个小时多少分钟
     */
    public static int TIME_HOUR_MINUTE = 60;
    /**
     * 游戏时间设定 - 一分钟多少秒
     */
    public static int TIME_MINUTE_SECOND = 1;

}
