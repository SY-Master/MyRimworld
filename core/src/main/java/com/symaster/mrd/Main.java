package com.symaster.mrd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.g2d.ViewportNodeOrthographic;
import com.symaster.mrd.g2d.tansform.TransformMove;
import com.symaster.mrd.g2d.tansform.TransformZoom;
import com.symaster.mrd.game.*;
import com.symaster.mrd.game.entity.GameGenerateData;
import com.symaster.mrd.game.entity.Save;
import com.symaster.mrd.game.service.ai.AI;
import com.symaster.mrd.game.ui.Loading;
import com.symaster.mrd.game.ui.MainMenu;
import com.symaster.mrd.game.ui.MainStageUI;
import com.symaster.mrd.input.InputBridge;
import com.symaster.mrd.input.RollerDragInput;
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

    private Save save;
    private MainMenu mainMenu;
    private MainStageUI gui;

    private Skin skin;
    private AssetManager assetManager;
    private Loading loading;
    private GameGenerateProcessor gameGenerateProcessor;
    private AsyncExecutor asyncExecutor;
    private ViewportNodeOrthographic camera;
    private AI ai;

    public ViewportNodeOrthographic buildCamera() {
        WASDInput wasdInput = new WASDInput();

        TransformMove transformMove = new TransformMove(wasdInput.getVector2());
        transformMove.setSpeed(UnitUtil.ofM(18));
        transformMove.setIgnoreTimeScale(true);

        ViewportNodeOrthographic camera = new ViewportNodeOrthographic(960, 540);

        RollerDragInput rollerDragInput = new RollerDragInput(camera);

        camera.add(rollerDragInput);
        camera.add(wasdInput);
        camera.add(transformMove);

        TransformZoom nodes = new TransformZoom(camera.getCamera(), camera) {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                boolean scrolled = super.scrolled(amountX, amountY);

                // 相机越高，移动越快
                if (camera.getWorldRectangle().getWidth() > 0 && scrolled) {
                    transformMove.setSpeed(camera.getWorldRectangle().getWidth() * 0.25f);
                }

                return scrolled;
            }
        };

        camera.add(nodes);
        return camera;
    }

    /**
     * 构建 Skin
     */
    public Skin buildSkin(AssetManager assetManager) {
        Skin skin = new Skin();

        for (int fontSize : SystemConfig.FONT_SIZES) {
            BitmapFont font = textLoad(SystemConfig.TEXT_PATH, SystemConfig.FONT_PATH, fontSize);
            skin.add("font-" + fontSize, font);
        }

        /// Drawables
        NinePatchDrawable nDChecked = new NinePatchDrawable(new NinePatch(assetManager.get("default-checked.9.png", Texture.class), 2, 2, 2, 2));
        NinePatchDrawable nDFocused = new NinePatchDrawable(new NinePatch(assetManager.get("default-focused.9.png", Texture.class), 2, 2, 2, 2));
        NinePatchDrawable nDUp = new NinePatchDrawable(new NinePatch(assetManager.get("default-up.9.png", Texture.class), 2, 2, 2, 2));
        NinePatchDrawable border0 = new NinePatchDrawable(new NinePatch(assetManager.get("border0.png", Texture.class), 1, 1, 1, 1));
        SolidColorDrawable back_05 = new SolidColorDrawable(new Color(0, 0, 0, 0.5f));
        SolidColorDrawable white = new SolidColorDrawable(new Color(1, 1, 1, 1));

        /// Fonts
        BitmapFont font16 = skin.getFont("font-16");

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(nDUp, nDChecked, nDUp, font16);
        textButtonStyle.focused = nDFocused;
        skin.add("default", textButtonStyle);

        Label.LabelStyle style = new Label.LabelStyle(font16, new Color(1f, 1f, 1f, 1f));
        style.background = back_05;
        skin.add("nameLabel", style);

        TextButton.TextButtonStyle switchBtn = new TextButton.TextButtonStyle(back_05, back_05, back_05, font16);
        skin.add("switch", switchBtn);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = border0;
        progressBarStyle.knobBefore = white;
        skin.add("default", progressBarStyle);

        return skin;
    }

    public void loadSkin() {
        skin = buildSkin(assetManager);
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

    public void loadGUI() {
        gui = new MainStageUI(skin);
        GameSingleData.inputBridge.add(gui);
    }

    public void loadMainMenu() {
        mainMenu = new MainMenu(skin, assetManager);
        mainMenu.act();
        mainMenu.getPlayGameSetting().getPlayGame().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playGameClick();
            }
        });
        GameSingleData.inputBridge.add(mainMenu);
    }

    /**
     * 开始游戏
     */
    public void playGameClick() {
        if (save != null) {
            save.dispose();
            save = null;
        }

        GameGenerateData gameGenerateData = new GameGenerateData();
        gameGenerateData.mapSeed = UUID.randomUUID().toString();
        gameGenerateData.assetManager = assetManager;
        gameGenerateData.skin = skin;
        gameGenerateData.ai = ai;
        asyncExecutor.submit((gameGenerateProcessor = new GameGenerateProcessor(gameGenerateData)));

        GameSingleData.loadingType = LoadingType.GamePlayLoading;
        GameSingleData.gamePageStatus = GamePageStatus.Loading;
    }

    public void applicationRunnerLoading(float delta) {
        if (!assetManager.update(17)) {
            loading.setProgressValue(assetManager.getProgress());
        } else {
            loadSkin();
            loadMainMenu();
            loadGUI();
            loading.setProgressValue(1f);
            GameSingleData.gamePageStatus = GamePageStatus.Menu;
        }

        loading.logic(delta);
        loading.render();
    }

    public void gamePlayLoading(float delta) {
        if (gameGenerateProcessor != null && !gameGenerateProcessor.update(17)) {
            loading.setProgressValue(gameGenerateProcessor.getProgress());
        } else if (gameGenerateProcessor != null) {
            this.save = gameGenerateProcessor.getSave();
            this.save.getScene().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.save.getScene().add(camera, Groups.CAMERA);
            this.ai.setScene(this.save.getScene());
            this.gui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.gui.setScene(save.getScene());

            this.loading.setProgressValue(1f);
            this.loading.logic(delta);
            this.loading.render();

            GameSingleData.gamePageStatus = GamePageStatus.Game;
        }

        loading.logic(delta);
        loading.render();
    }

    public void menuRender(float delta) {
        mainMenu.logic(delta);
        mainMenu.render();
    }

    public void gameRender(float delta) {
        // 处理场景的逻辑
        save.getScene().logic(delta);
        // 处理GUI的逻辑
        gui.logic(delta);

        // 绘制相机
        camera.render();
        // 绘制GUI
        gui.render();
    }

    @Override
    public void create() {

        // 界面状态为加载界面
        GameSingleData.loadingType = LoadingType.ApplicationRunnerLoading;
        GameSingleData.gamePageStatus = GamePageStatus.Loading;
        GameSingleData.inputBridge = new InputBridge();

        Gdx.input.setInputProcessor(GameSingleData.inputBridge);

        assetManager = new AssetManager();
        assetManager.load("TX Tileset Grass.png", Texture.class);
        assetManager.load("user.png", Texture.class);
        assetManager.load("default-checked.9.png", Texture.class);
        assetManager.load("default-focused.9.png", Texture.class);
        assetManager.load("default-up.9.png", Texture.class);
        assetManager.load("log.png", Texture.class);
        assetManager.load("white.png", Texture.class);
        assetManager.load("left.png", Texture.class);
        assetManager.load("Water.png", Texture.class);
        assetManager.load("TX Plant.png", Texture.class);
        assetManager.load("TX Shadow Plant.png", Texture.class);
        assetManager.load("border0.png", Texture.class);

        this.ai = new AI();
        this.loading = new Loading();
        this.asyncExecutor = new AsyncExecutor(1);
        this.camera = buildCamera();
        GameSingleData.positionConverter = camera.getPositionConverter();
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

        if (camera != null) {
            camera.getViewport().update(width, height);
        }

        // fillViewport.getViewport().update(width, height);
        // gui.resize(width, height);
        // scene.resize(width, height);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameSingleData.gamePageStatus.render(this, delta);
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
