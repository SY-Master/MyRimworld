package com.symaster.mrd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yinmiao
 * @since 2025/3/18
 */
public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static String repairJsonStr(String string) {
        int i0 = string.indexOf("[");
        int i1 = string.indexOf("{");

        boolean i0Available = i0 >= 0;
        boolean i1Available = i1 >= 0;

        if (!i0Available && !i1Available) {
            logger.error(string);
            throw new IllegalArgumentException("Json格式错误");
        }

        // 两种括号都存在的情况下
        boolean allAvailable = i0Available && i1Available;

        int enable;
        if (allAvailable) {
            if (i0 < i1) {
                enable = 0;
            } else {
                enable = 1;
            }
        } else {
            if (i0Available) {
                enable = 0;
            } else {
                enable = 1;
            }
        }

        if (enable == 0) {
            int i = string.lastIndexOf("]");
            if (i < 0) {
                logger.error(string);
                throw new IllegalArgumentException("Json格式错误");
            }

            return string.substring(i0, i + 1);
        } else {
            int i = string.lastIndexOf("}");
            if (i < 0) {
                logger.error(string);
                throw new IllegalArgumentException("Json格式错误");
            }

            return string.substring(i1, i + 1);
        }
    }

}
