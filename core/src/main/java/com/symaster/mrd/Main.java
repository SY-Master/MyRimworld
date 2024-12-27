package com.symaster.mrd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.symaster.mrd.g2d.NinePatchNode;
import com.symaster.mrd.g2d.Scene;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.g2d.ViewportNode;
import com.symaster.mrd.g2d.tansform.TransformMove;
import com.symaster.mrd.g2d.tansform.TransformZoom;
import com.symaster.mrd.gui.MainStageUI;
import com.symaster.mrd.input.InputFactory;
import com.symaster.mrd.input.WASDInput;
import com.symaster.mrd.util.GdxText;
import com.symaster.mrd.util.UnitUtil;

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
        InputFactory inputFactory = new InputFactory();
        Gdx.input.setInputProcessor(inputFactory);
        skin = defaultSkin();

        gui = MainStageUI.create(skin);
        inputFactory.add(gui);

        Texture texture = new Texture(Gdx.files.internal("user.png"));

        scene = new Scene();
        scene.setInputFactory(inputFactory);

        addMap(scene);

        Sprite sprite1 = new Sprite(texture);
        sprite1.setSize(UnitUtil.ofM(1), UnitUtil.ofM(1));
        sprite1.setColor(new Color(255, 0, 0, 255));
        SpriteNode nodes = new SpriteNode(sprite1);
        nodes.setActivityBlockSize(3);
        nodes.setPositionX(100);
        nodes.setPositionY(100);

        Sprite sprite = new Sprite(texture);
        sprite.setSize(UnitUtil.ofM(1), UnitUtil.ofM(1));
        sprite.setColor(new Color(0, 255, 0, 255));
        SpriteNode nodes1 = new SpriteNode(sprite);
        nodes1.setActivityBlockSize(1);
        nodes1.setPositionX(100);
        nodes1.setPositionY(200);

        scene.add(nodes);
        scene.add(nodes1);

        fillViewport = new ViewportNode(960, 540);
        fillViewport.setActivityBlockSize(1);
        WASDInput wasdInput = new WASDInput();
        fillViewport.add(wasdInput);

        TransformMove transformMove = new TransformMove(wasdInput.getVector2(), fillViewport);
        transformMove.setSpeed(UnitUtil.ofM(18));
        fillViewport.add(transformMove);

        TransformZoom nodes2 = new TransformZoom((OrthographicCamera) fillViewport.getCamera(), fillViewport);
        fillViewport.add(nodes2);

        nodes1.add(fillViewport);
    }

    private void addMap(Scene scene) {
        Texture borderTexture = new Texture(Gdx.files.internal("border.png"));
        float w = UnitUtil.ofM(1);
        float h = UnitUtil.ofM(1);

        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(new NinePatch(borderTexture, 1, 1, 1, 1));

        for (int x = -100; x < 100; x++) {
            for (int y = -100; y < 100; y++) {
                NinePatchNode nodes1 = new NinePatchNode(ninePatchDrawable);
                nodes1.setWidth(w);
                nodes1.setHeight(h);
                nodes1.setZIndex(-1);
                nodes1.setPosition(x * w, y * w);
                scene.add(nodes1);
            }
        }


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

        // 处理场景的逻辑
        scene.logic(delta);
        // 处理GUI的逻辑
        gui.logic(delta);

        // 绘制场景
        scene.render();
        // 绘制GUI
        gui.render();
    }

    @Override
    public void dispose() {
        gui.dispose();
        skin.dispose();
    }
}
