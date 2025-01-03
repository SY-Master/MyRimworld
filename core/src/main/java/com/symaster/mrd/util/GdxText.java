package com.symaster.mrd.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.symaster.mrd.SystemConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class GdxText {

    private static Map<String, String> textCollect;

    public static String get(String key) {
        if (key == null || key.isEmpty()) {
            return "";
        }

        if (textCollect == null) {
            textInit();
        }

        String s = textCollect.get(key);
        if (s == null) {
            return "";
        }

        return s;
    }

    public static String val(String val) {
        if (val == null || val.isEmpty()) {
            return "";
        }

        String rtn1 = get(val);
        return rtn1.isEmpty() ? val : rtn1;
    }

    public static List<String> values() {
        if (textCollect == null) {
            textInit();
        }

        return new ArrayList<>(textCollect.values());
    }

    private static void textInit() {
        FileHandle fileHandle = Gdx.files.internal(SystemConfig.TEXT_PATH);
        if (!fileHandle.exists()) {
            textCollect = new HashMap<>();
            return;
        }

        try (InputStream textInput = fileHandle.read();
             InputStreamReader isr = new InputStreamReader(textInput, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            textCollect = br.lines().collect(Collectors.toMap(e -> {
                int i = e.indexOf("=");
                return e.substring(0, i).trim();
            }, e -> {
                int i = e.indexOf("=");
                return e.substring(i + 1).trim();
            }));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() {
        textCollect = null;
    }
}

