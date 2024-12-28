package com.symaster.mrd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.symaster.mrd.g2d.*;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerate;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerateProcessor;
import com.symaster.mrd.g2d.tansform.TransformMove;
import com.symaster.mrd.g2d.tansform.TransformZoom;
import com.symaster.mrd.game.BlockMapGenerateProcessorImpl;
import com.symaster.mrd.gui.MainStageUI;
import com.symaster.mrd.input.InputFactory;
import com.symaster.mrd.input.WASDInput;
import com.symaster.mrd.util.GdxText;
import com.symaster.mrd.util.UnitUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private MainStageUI gui;
    private Skin skin;
    private Scene scene;
    private ViewportNodeOrthographic fillViewport;
    private AssetManager assetManager;

    @Override
    public void create() {

        assetManager = new AssetManager();
        assetManager.load("TX Tileset Grass.png", Texture.class);
        assetManager.load("user.png", Texture.class);
        assetManager.load("default-checked.9.png", Texture.class);
        assetManager.load("default-focused.9.png", Texture.class);
        assetManager.load("default-up.9.png", Texture.class);

        InputFactory inputFactory = new InputFactory();
        Gdx.input.setInputProcessor(inputFactory);

        skin = defaultSkin(assetManager);

        gui = MainStageUI.create(skin);
        inputFactory.add(gui);

        // assetManager.load("user.png", Texture.class);

        // Texture texture1 = assetManager.get("user.png", Texture.class);

        Texture userTexture = assetManager.get("user.png", Texture.class);

        BlockMapGenerateProcessorImpl bm = new BlockMapGenerateProcessorImpl(assetManager);


        scene = new Scene();
        scene.create();
        scene.setBlockMapGenerateProcessor(bm);
        scene.setInputFactory(inputFactory);

        Sprite sprite1 = new Sprite(userTexture);
        sprite1.setSize(UnitUtil.ofM(1), UnitUtil.ofM(1));
        sprite1.setColor(new Color(255, 0, 0, 255));
        SpriteNode nodes = new SpriteNode(sprite1);
        nodes.setActivityBlockSize(3);
        nodes.setPositionX(100);
        nodes.setPositionY(100);

        Sprite sprite = new Sprite(userTexture);
        sprite.setSize(UnitUtil.ofM(1), UnitUtil.ofM(1));
        sprite.setColor(new Color(0, 255, 0, 255));
        SpriteNode nodes1 = new SpriteNode(sprite);
        nodes1.setActivityBlockSize(1);
        nodes1.setPositionX(100);
        nodes1.setPositionY(200);

        scene.add(nodes);
        scene.add(nodes1);

        fillViewport = new ViewportNodeOrthographic(960, 540);
        fillViewport.setActivityBlockSize(1);
        WASDInput wasdInput = new WASDInput();
        fillViewport.add(wasdInput);

        TransformMove transformMove = new TransformMove(wasdInput.getVector2(), fillViewport);
        transformMove.setSpeed(UnitUtil.ofM(18));
        fillViewport.add(transformMove);

        TransformZoom nodes2 = new TransformZoom(fillViewport.getCamera(), fillViewport);
        fillViewport.add(nodes2);

        nodes1.add(fillViewport);
    }

    // private void addMap(Scene scene) {
    //     Texture borderTexture = new Texture(Gdx.files.internal("TX Tileset Grass.png"));
    //     TextureRegion textureRegion = new TextureRegion(borderTexture, 32, 32, 32, 32);
    //
    //     float w = UnitUtil.ofM(1);
    //     float h = UnitUtil.ofM(1);
    //
    //     NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(new NinePatch(textureRegion, 1, 1, 1, 1));
    //
    //     int size = 500;
    //     for (int x = -size; x < size; x++) {
    //         for (int y = -size; y < size; y++) {
    //             NinePatchNode nodes1 = new NinePatchNode(ninePatchDrawable);
    //             nodes1.setWidth(w);
    //             nodes1.setHeight(h);
    //             nodes1.setZIndex(-1);
    //             nodes1.setPosition(x * w, y * w);
    //             scene.add(nodes1);
    //         }
    //     }
    // }

    Skin defaultSkin(AssetManager assetManager) {

        BitmapFont defaultFont = textLoad(SystemConfig.TEXT_PATH, SystemConfig.FONT_PATH, SystemConfig.FONT_SIZE);
        NinePatch checked = new NinePatch(assetManager.get("default-checked.9.png", Texture.class), 2, 2, 2, 2);
        NinePatch focused = new NinePatch(assetManager.get("default-focused.9.png", Texture.class), 2, 2, 2, 2);
        NinePatch up = new NinePatch(assetManager.get("default-up.9.png", Texture.class), 2, 2, 2, 2);

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
        // 加载资源
        if (assetManager.update(17)) {

        }

        float delta = SystemConfig.TIME_SCALE * Gdx.graphics.getDeltaTime();
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
