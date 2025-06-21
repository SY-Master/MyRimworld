package com.symaster.mrd.g2d;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2025/6/12
 */
public class DynamicFont {

    private final Set<Character> charCaches = new HashSet<>();
    private final Set<Character> newChars = new HashSet<>();
    private final int fontSize;
    private final FreeTypeFontGenerator generator;
    private BitmapFont bitmapFont;

    public DynamicFont(FileHandle ttfFont, int fontSize) {
        this.generator = new FreeTypeFontGenerator(ttfFont);
        this.bitmapFont = generator.generateFont(defaultFreeTypeFontParameter());
        this.fontSize = fontSize;
    }

    public DynamicFont(FreeTypeFontGenerator generator, int fontSize) {
        this.generator = generator;
        this.bitmapFont = generator.generateFont(defaultFreeTypeFontParameter());
        this.fontSize = fontSize;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public int getFontSize() {
        return fontSize;
    }

    private FreeTypeFontGenerator.FreeTypeFontParameter defaultFreeTypeFontParameter() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize; // 增加字体大小
        parameter.characters = "你好世界ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // 自定义字符集
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.hinting = FreeTypeFontGenerator.Hinting.Full; // 启用全提示
        parameter.renderCount = 2; // 提高渲染质量
        parameter.genMipMaps = true; // 生成 Mipmap
        return parameter;
    }

    public synchronized void generate(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }

        newChars.clear();
        for (Character c : text.toCharArray()) {
            if (charCaches.contains(c)) {
                continue;
            }
            newChars.add(c);
        }

        if (newChars.isEmpty()) {
            return;
        }

        charCaches.addAll(newChars);
        newChars.clear();

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = defaultFreeTypeFontParameter();
        parameter.characters = charCaches.stream().map(String::valueOf).collect(Collectors.joining());

        if (bitmapFont != null) {
            bitmapFont.dispose();
            bitmapFont = null;
        }

        bitmapFont = generator.generateFont(parameter);
    }

}
