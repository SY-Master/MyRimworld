package com.symaster.mrd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.symaster.mrd.g2d.ViewportNodeOrthographic;
import com.symaster.mrd.g2d.tansform.TransformMove;
import com.symaster.mrd.g2d.tansform.TransformZoom;
import com.symaster.mrd.game.GameGenerateProcessor;
import com.symaster.mrd.game.data.GameGenerateData;
import com.symaster.mrd.game.data.Save;
import com.symaster.mrd.game.ui.Loading;
import com.symaster.mrd.game.ui.MainMenu;
import com.symaster.mrd.gui.MainStageUI;
import com.symaster.mrd.input.InputFactory;
import com.symaster.mrd.input.WASDInput;
import com.symaster.mrd.util.GdxText;
import com.symaster.mrd.util.UnitUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private MainStageUI gui;
    private Skin skin;
    private AssetManager assetManager;
    private Loading loading;
    private Save save;
    private Status status;
    private MainMenu mainMenu;
    private InputFactory inputFactory;
    private GameGenerateProcessor gameGenerateProcessor;
    private AsyncExecutor asyncExecutor;
    private SpriteBatch spriteBatch;
    private ViewportNodeOrthographic cam;

    private ViewportNodeOrthographic getCam() {
        ViewportNodeOrthographic cam = new ViewportNodeOrthographic(960, 540);
        cam.setLimit2activityBlock(true);
        WASDInput wasdInput = new WASDInput();
        cam.add(wasdInput);
        TransformMove transformMove = new TransformMove(wasdInput.getVector2(), cam);
        transformMove.setSpeed(UnitUtil.ofM(18));
        cam.add(transformMove);

        cam.add(new TransformZoom(cam.getCamera(), cam));
        return cam;
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor((inputFactory = new InputFactory()));

        assetManager = new AssetManager();
        assetManager.load("TX Tileset Grass.png", Texture.class);
        assetManager.load("user.png", Texture.class);
        assetManager.load("default-checked.9.png", Texture.class);
        assetManager.load("default-focused.9.png", Texture.class);
        assetManager.load("default-up.9.png", Texture.class);
        assetManager.load("log.png", Texture.class);
        assetManager.load("white.png", Texture.class);
        assetManager.load("left.png", Texture.class);

        this.loading = new Loading();
        this.asyncExecutor = new AsyncExecutor(1);
        this.spriteBatch = new SpriteBatch();
        this.cam = getCam();
        this.status = Status.MainLoading;

        // skin = defaultSkin(assetManager);

        // gui = MainStageUI.create(skin);
        // inputFactory.add(gui);
        //
        // // assetManager.load("user.png", Texture.class);
        //
        // // Texture texture1 = assetManager.get("user.png", Texture.class);
        //
        // Texture userTexture = assetManager.get("user.png", Texture.class);
        //
        // BlockMapGenerateProcessorImpl bm = new BlockMapGenerateProcessorImpl(assetManager);


        // scene = new Scene();
        // scene.create();
        // scene.setBlockMapGenerateProcessor(bm);
        // scene.setInputFactory(inputFactory);

        // Sprite sprite1 = new Sprite(userTexture);
        // sprite1.setSize(UnitUtil.ofM(1), UnitUtil.ofM(1));
        // sprite1.setColor(new Color(255, 0, 0, 255));
        // SpriteNode nodes = new SpriteNode(sprite1);
        // nodes.setActivityBlockSize(3);
        // nodes.setPositionX(100);
        // nodes.setPositionY(100);
        //
        // Sprite sprite = new Sprite(userTexture);
        // sprite.setSize(UnitUtil.ofM(1), UnitUtil.ofM(1));
        // sprite.setColor(new Color(0, 255, 0, 255));
        // SpriteNode nodes1 = new SpriteNode(sprite);
        // nodes1.setActivityBlockSize(1);
        // nodes1.setPositionX(100);
        // nodes1.setPositionY(200);

        // scene.add(nodes);
        // scene.add(nodes1);


        // nodes1.add(fillViewport);
    }

    public Skin defaultSkin(AssetManager assetManager) {
        Skin skin = new Skin();

        for (int fontSize : SystemConfig.FONT_SIZES) {
            BitmapFont font = textLoad(SystemConfig.TEXT_PATH, SystemConfig.FONT_PATH, fontSize);
            skin.add("font-" + fontSize, font);
        }

        NinePatch checked = new NinePatch(assetManager.get("default-checked.9.png", Texture.class), 2, 2, 2, 2);
        NinePatch focused = new NinePatch(assetManager.get("default-focused.9.png", Texture.class), 2, 2, 2, 2);
        NinePatch up = new NinePatch(assetManager.get("default-up.9.png", Texture.class), 2, 2, 2, 2);

        NinePatchDrawable nDChecked = new NinePatchDrawable(checked);
        NinePatchDrawable nDFocused = new NinePatchDrawable(focused);
        NinePatchDrawable nDUp = new NinePatchDrawable(up);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(nDUp, nDChecked, nDUp, skin.getFont("font-16"));
        textButtonStyle.focused = nDFocused;
        skin.add("default", textButtonStyle);
        return skin;
    }

    private void loadSkin() {
        skin = defaultSkin(assetManager);
        status = Status.SkinLoadingFinish;
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
        if (loading != null) {
            loading.resize(width, height);
        }

        if (mainMenu != null) {
            mainMenu.resize(width, height);
        }

        if (gui != null) {
            gui.resize(width, height);
        }

        if (save != null && save.getScene() != null) {
            save.getScene().resize(width, height);
        }

        if (cam != null) {
            cam.getViewport().update(width, height);
        }

        // fillViewport.getViewport().update(width, height);
        // gui.resize(width, height);
        // scene.resize(width, height);
    }

    @Override
    public void render() {
        float delta = SystemConfig.TIME_SCALE * Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 加载资源
        if (!assetManager.update(17)) {
            loading.setProgressValue(assetManager.getProgress());
            loading.logic(delta);
            loading.render();
        } else if (status == Status.MainLoading) {
            loadSkin();
            loading.setProgressValue(1f);
            loading.logic(delta);
            loading.render();
        } else if (status == Status.SkinLoadingFinish) {
            loadMainMenu();
            loading.setProgressValue(1f);
            loading.logic(delta);
            loading.render();
        } else if (status == Status.MainMenuLoadingFinish) {
            mainMenu.logic(delta);
            mainMenu.render();
        } else if (status == Status.GameGenerating && gameGenerateProcessor != null && !gameGenerateProcessor.update(17)) {
            loading.setProgressValue(gameGenerateProcessor.getProgress());
            loading.logic(delta);
            loading.render();
        } else if (status == Status.GameGenerating && gameGenerateProcessor != null) {
            this.save = gameGenerateProcessor.getSave();
            this.save.getScene().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.save.getScene().add(cam);
            this.gui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.gui.setScene(save.getScene());

            this.loading.setProgressValue(1f);
            this.loading.logic(delta);
            this.loading.render();
            this.status = Status.GameReadyGo;
        } else if (save != null && gui != null) {
            // 处理场景的逻辑
            save.getScene().logic(delta);
            // 处理GUI的逻辑
            gui.logic(delta);

            // 绘制场景
            save.getScene().render();
            // 绘制GUI
            gui.render();
        }
    }

    private void loadMainMenu() {
        mainMenu = new MainMenu(skin, assetManager);
        mainMenu.act();
        mainMenu.getPlayGameSetting().getPlayGame().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playGameClick();
            }
        });
        inputFactory.add(mainMenu);
        status = Status.MainMenuLoadingFinish;
    }

    /**
     * 开始游戏
     */
    private void playGameClick() {
        if (save != null) {
            save.dispose();
            save = null;
        }

        if (gui == null) {
            gui = new MainStageUI(skin);
            inputFactory.add(gui);
        }

        GameGenerateData gameGenerateData = new GameGenerateData();
        gameGenerateData.mapSeed = UUID.randomUUID().toString();
        gameGenerateData.spriteBatch = this.spriteBatch;
        gameGenerateData.inputFactory = inputFactory;
        gameGenerateData.assetManager = assetManager;
        asyncExecutor.submit((gameGenerateProcessor = new GameGenerateProcessor(gameGenerateData)));

        status = Status.GameGenerating;
    }

    @Override
    public void dispose() {
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (loading != null) {
            loading.dispose();
        }
        if (mainMenu != null) {
            mainMenu.dispose();
        }
        if (gui != null) {
            gui.dispose();
        }
    }
}
