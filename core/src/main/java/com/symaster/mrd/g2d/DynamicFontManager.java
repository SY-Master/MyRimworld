package com.symaster.mrd.g2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.symaster.mrd.SystemConfig;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2025/6/12
 */
public class DynamicFontManager {


    private final Map<FontEnum, List<DynamicFont>> fontMap = new HashMap<>();
    private final Map<FontEnum, FreeTypeFontGenerator> ffMap = new HashMap<>();
    private final Map<FontEnum, Set<Character>> charFonts = new HashMap<>();
    private final Map<FontEnum, Map<Integer, Set<Character>>> charSizes = new HashMap<>();
    private final Set<Character> chars = new HashSet<>();
    private final Set<Character> newChars = new HashSet<>();

    public static DynamicFontManager create(String... defaultContent) {
        FileHandle internal = Gdx.files.internal("fonts/font.ini");

        Configurations configurations = new Configurations();
        try (InputStream read = internal.read();
             InputStreamReader inputStreamReader = new InputStreamReader(read, StandardCharsets.UTF_8);) {

            INIConfiguration ini = new INIConfiguration();
            ini.read(inputStreamReader);

            SubnodeConfiguration subnodeConfiguration = ini.getSection("fontMap");

            DynamicFontManager rtn = new DynamicFontManager();

            for (FontEnum value : FontEnum.values()) {
                String baseFont = subnodeConfiguration.get(String.class, value.getKey());
                if (baseFont == null || baseFont.isEmpty()) {
                    continue;
                }

                List<String> list = new ArrayList<>();
                list.add("fonts");
                list.addAll(Arrays.asList(baseFont.split("[/\\\\]")));
                String baseFontPath = String.join("/", list);

                for (int defaultSize : SystemConfig.FONT_SIZES.getFontSizes()) {
                    rtn.init(value, baseFontPath, null, defaultSize, defaultContent);
                }
            }

            return rtn;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    private DynamicFont init(FontEnum fontEnum, String baseFontPath, FreeTypeFontGenerator freeTypeFontGenerator, int size, String... defaultContent) {
        Gdx.app.log("DynamicFont-init",
                    String.format("fontEnum:%s, baseFontPath:%s, freeTypeFontGenerator:%s, size:%s, defaultContent:%s", fontEnum, baseFontPath, freeTypeFontGenerator,
                                  size, Arrays.toString(defaultContent)));

        List<DynamicFont> dynamicFonts = fontMap.computeIfAbsent(fontEnum, k -> new ArrayList<>());

        DynamicFont oldDynamicFont = dynamicFonts.stream()
                                                 .filter(e -> e.getFontSize() == size)
                                                 .findFirst()
                                                 .orElse(null);
        if (oldDynamicFont != null) {
            return oldDynamicFont;
        }

        FreeTypeFontGenerator ff;

        if (freeTypeFontGenerator != null) {
            ff = freeTypeFontGenerator;
        } else {
            ff = new FreeTypeFontGenerator(Gdx.files.internal(baseFontPath));
        }

        DynamicFont dynamicFont = new DynamicFont(ff, size);
        dynamicFonts.add(dynamicFont);
        ffMap.put(fontEnum, ff);

        if (defaultContent != null) {
            for (String content : defaultContent) {
                dynamicFont.generate(content);
            }
        }

        return dynamicFont;
    }

    public DynamicFont getDynamicFont(FontEnum fontEnum, int size) {
        if (!ffMap.containsKey(fontEnum)) {
            throw new IllegalArgumentException(String.format("%s Is Null", fontEnum.getKey()));
        }

        List<DynamicFont> dynamicFonts = fontMap.get(fontEnum);
        if (dynamicFonts == null || dynamicFonts.isEmpty()) {
            return init(fontEnum, null, ffMap.get(fontEnum), size);
        }

        DynamicFont dynamicFont = dynamicFonts.stream().filter(e -> e.getFontSize() == size).findFirst().orElse(null);
        if (dynamicFont != null) {
            return dynamicFont;
        }

        return init(fontEnum, null, ffMap.get(fontEnum), size);
    }

    public BitmapFont getFont(FontEnum fontEnum, int size) {
        return getDynamicFont(fontEnum, size).getBitmapFont();
    }

    public synchronized void append(FontEnum fontEnum, Integer size, String content) {
        if (content == null || content.isEmpty()) {
            return;
        }
        Set<Character> oldChars = getOldChars(fontEnum, size);

        newChars.clear();
        for (Character c : content.toCharArray()) {
            if (oldChars.contains(c)) {
                continue;
            }
            newChars.add(c);
        }

        String collect = newChars.stream().map(String::valueOf).collect(Collectors.joining());
        chars.addAll(newChars);
        newChars.clear();

        List<DynamicFont> dynamicFonts = getDynamicFonts(fontEnum, size);

        for (DynamicFont dynamicFont : dynamicFonts) {
            dynamicFont.generate(collect);
        }
    }

    private List<DynamicFont> getDynamicFonts(FontEnum fontEnum, Integer size) {
        if (fontEnum == null) {
            return fontMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        }

        if (!ffMap.containsKey(fontEnum)) {
            throw new IllegalArgumentException(String.format("%s Is Null", fontEnum.getKey()));
        }

        List<DynamicFont> dynamicFonts = fontMap.get(fontEnum);
        if (dynamicFonts == null || dynamicFonts.isEmpty()) {
            String oldContent = getOldContent(fontEnum, size);

            for (int fontSize : SystemConfig.FONT_SIZES.getFontSizes()) {
                init(fontEnum, null, ffMap.get(fontEnum), fontSize, oldContent);
            }
        }

        List<DynamicFont> dynamicFonts1 = fontMap.get(fontEnum);
        if (size == null) {
            return dynamicFonts1;
        }

        DynamicFont dynamicFont = dynamicFonts1.stream().filter(e -> e.getFontSize() == size).findFirst().orElse(null);
        if (dynamicFont != null) {
            return Collections.singletonList(dynamicFont);
        }

        String oldContent = getOldContent(fontEnum, size);
        DynamicFont init = init(fontEnum, null, ffMap.get(fontEnum), size, oldContent);

        return Collections.singletonList(init);
    }

    private String getOldContent(FontEnum fontEnum, Integer size) {
        Set<Character> oldChars = getOldChars(fontEnum, size);
        if (oldChars.isEmpty()) {
            return null;
        }

        return oldChars.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private Set<Character> getOldChars(FontEnum fontEnum, Integer size) {

        if (size != null) {
            if (fontEnum == null) {
                throw new IllegalArgumentException("fontEnum Is Empty");
            }

            return charSizes.computeIfAbsent(fontEnum, k -> new HashMap<>())
                            .computeIfAbsent(size, k -> new HashSet<>());
        } else {
            if (fontEnum != null) {
                return charFonts.computeIfAbsent(fontEnum, k -> new HashSet<>());
            }

            return chars;
        }
    }

}
