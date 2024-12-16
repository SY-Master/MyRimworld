package com.symaster.mrd.util;

import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.lang.Filter;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class ClassUtil {

    public static Set<Class<?>> scan(String packageName, Filter<Class<?>> classFilter) {
        ClassScanner scanner = new ClassScanner(packageName, classFilter);
        return scanner.scan().stream().filter(e -> {
            if (Modifier.isAbstract(e.getModifiers())) {
                return false;
            }
            return !e.isInterface();
        }).collect(Collectors.toSet());
    }
}
