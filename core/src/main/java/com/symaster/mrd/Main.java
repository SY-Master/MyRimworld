package com.symaster.mrd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.symaster.mrd.g2d.CameraNode;
import com.symaster.mrd.g2d.Scene;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.g2d.ViewportNode;
import com.symaster.mrd.gui.MainStageUI;
import com.symaster.mrd.util.GdxText;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private MainStageUI gui;
    private Skin skin;
    private Scene scene;
    private ViewportNode fillViewport;

    @Override
    public void create() {
        skin = defaultSkin();

        gui = MainStageUI.create(skin);
        Gdx.input.setInputProcessor(gui);

        Texture texture = new Texture(Gdx.files.internal("user.png"));

        scene = new Scene();

        Sprite sprite1 = new Sprite(texture);
        sprite1.setColor(new Color(255, 0, 0, 255));

        SpriteNode nodes = new SpriteNode(sprite1);
        nodes.setActivityBlockSize(3);
        nodes.setPositionX(100);
        nodes.setPositionY(100);

        Sprite sprite = new Sprite(texture);
        sprite.setColor(new Color(0, 255, 0, 255));

        SpriteNode nodes1 = new SpriteNode(sprite);
        nodes.setPositionX(100);
        nodes.setPositionY(200);

        scene.add(nodes);
        scene.add(nodes1);

        fillViewport = new ViewportNode(1920, 1080);
        scene.add(fillViewport);
    }

    Skin defaultSkin() {
        BitmapFont defaultFont = textLoad(SystemConfig.TEXT_PATH, SystemConfig.FONT_PATH, SystemConfig.FONT_SIZE);
        NinePatch checked = new NinePatch(new Texture(Gdx.files.internal("default-checked.9.png")), 2, 2, 2, 2);
        NinePatch focused = new NinePatch(new Texture(Gdx.files.internal("default-focused.9.png")), 2, 2, 2, 2);
        NinePatch up = new NinePatch(new Texture(Gdx.files.internal("default-up.9.png")), 2, 2, 2, 2);
        NinePatchDrawable nDChecked = new NinePatchDrawable(checked);
        NinePatchDrawable nDFocused = new NinePatchDrawable(focused);
        NinePatchDrawable nDUp = new NinePatchDrawable(up);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(nDUp, nDChecked, nDUp, defaultFont);
        textButtonStyle.focused = nDFocused;

        Skin skin = new Skin();
        skin.add("default", textButtonStyle);
        return skin;
    }

    public BitmapFont textLoad(String textPath, String fontPath, int size) {
        List<String> values = GdxText.values();

        String collect = values.stream()
                .flatMap(e -> Stream.of(e.toCharArray()))
                .map(String::new)
                .distinct()
                .collect(Collectors.joining());

        if (collect.isEmpty()) {
            return null;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = collect;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    @Override
    public void resize(int width, int height) {
        fillViewport.getViewport().update(width, height);
        gui.resize(width, height);
        scene.resize(width, height);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scene.act(delta);
        gui.act(delta);

        scene.render();
        gui.render();
    }

    @Override
    public void dispose() {
        gui.dispose();
        skin.dispose();
    }
}
