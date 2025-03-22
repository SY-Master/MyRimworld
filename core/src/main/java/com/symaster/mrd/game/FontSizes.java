package com.symaster.mrd.game;

/**
 * @author yinmiao
 * @since 2025/3/21
 */
public class FontSizes {

    private static final int[] FONT_SIZES = new int[]{8, 10, 12, 14, 16, 18, 20};

    public int[] getFontSizes() {
        return FONT_SIZES;
    }

    public int getOptimalSize(int size) {
        int optimalSize = 0;
        int abs = Integer.MAX_VALUE;

        for (int fontSize : FONT_SIZES) {
            int itemAbs = Math.abs(size - fontSize);
            if (itemAbs < abs) {
                abs = itemAbs;
                optimalSize = fontSize;
            }
        }

        return optimalSize;
    }

    public String getFontName(int fontSize) {
        return String.format("font-%s", fontSize);
    }

}
