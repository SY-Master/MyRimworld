package com.symaster.mrd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.symaster.mrd.g2d.CameraNode;
import com.symaster.mrd.g2d.Scene;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.gui.MainStageUI;
import com.symaster.mrd.util.GdxText;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private MainStageUI stage;
    private Skin skin;
    private Scene scene;

    @Override
    public void create() {
        skin = defaultSkin();

        stage = MainStageUI.create(skin);
        Gdx.input.setInputProcessor(stage);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        OrthographicCamera cam = new OrthographicCamera(30, 30 * (h / w));
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        scene = new Scene();
        scene.add(new SpriteNode(new Sprite(new Texture(Gdx.files.internal("male.png")))));
        scene.add(new SpriteNode(new Sprite(new Texture(Gdx.files.internal("female.png")))));
        scene.add(new CameraNode(cam));
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
        stage.resize(width, height);
    }

    @Override
    public void render() {
        // handleInput();
        // cam.update();

        scene.render();

        stage.render();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
