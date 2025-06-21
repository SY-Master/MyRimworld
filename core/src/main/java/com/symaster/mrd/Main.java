package com.symaster.mrd;

import com.alibaba.fastjson2.JSONObject;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.symaster.mrd.api.SkinProxy;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.g2d.DynamicFontManager;
import com.symaster.mrd.g2d.FontEnum;
import com.symaster.mrd.g2d.LazyBitmapFont;
import com.symaster.mrd.g2d.ViewportNodeOrthographic;
import com.symaster.mrd.g2d.tansform.TransformMove;
import com.symaster.mrd.g2d.tansform.TransformZoom;
import com.symaster.mrd.game.*;
import com.symaster.mrd.game.entity.GameGenerateData;
import com.symaster.mrd.game.entity.Save;
import com.symaster.mrd.game.service.PromptService;
import com.symaster.mrd.api.BaseStage;
import com.symaster.mrd.game.ui.GameUI;
import com.symaster.mrd.game.ui.Loading;
import com.symaster.mrd.input.InputBridge;
import com.symaster.mrd.input.RollerDragInput;
import com.symaster.mrd.input.WASDInput;
import com.symaster.mrd.util.GdxText;
import com.symaster.mrd.util.UnitUtil;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private Save save;
    // private MainMenu mainMenu;
    private GameUI gui;

    private BaseStage homePage;

    private Skin skin;
    // private AssetManager assetManager;
    private Loading loading;
    private GameGenerateProcessor gameGenerateProcessor;
    private AsyncExecutor asyncExecutor;
    private ViewportNodeOrthographic camera;

    // public void applicationRunnerLoading(float delta) {
    //     if (!assetManager.update(17)) {
    //         loading.setProgressValue(assetManager.getProgress());
    //     } else {
    //         loadSkin();
    //         // loadMainMenu();
    //         loadGUI();
    //         loading.setProgressValue(1f);
    //         // GameSingleData.gamePageStatus = GamePageStatus.Menu;
    //     }
    //
    //     loading.logic(delta);
    //     loading.render();
    // }

    public SkinProxy loadSkin() {
        skin = buildSkin();
        return new SkinProxy(skin);
    }

    /**
     * 构建 Skin。本项目里面Skin主要用户GUI的皮肤
     *
     * @return Skin
     */
    public Skin buildSkin() {
        Skin skin = new Skin();

        FileHandle internal = Gdx.files.internal("fonts/font.ini");

        skin.add("default", new LazyBitmapFont(new FreeTypeFontGenerator(internal), 16));
        skin.add("18", new LazyBitmapFont(new FreeTypeFontGenerator(internal), 18));
        skin.add("20", new LazyBitmapFont(new FreeTypeFontGenerator(internal), 20));
        skin.add("22", new LazyBitmapFont(new FreeTypeFontGenerator(internal), 22));
        skin.add("default", new Color(1f, 1f, 1f, 1f));

        // GameSingleData.mrAssetManager.getGlobal("")
        Texture checked = GameSingleData.mrAssetManager.getGlobal("checked", Texture.class);
        Texture focused = GameSingleData.mrAssetManager.getGlobal("focused", Texture.class);
        Texture up = GameSingleData.mrAssetManager.getGlobal("up", Texture.class);
        Texture border0 = GameSingleData.mrAssetManager.getGlobal("border0", Texture.class);

        /// Drawables
        NinePatchDrawable nDChecked = new NinePatchDrawable(new NinePatch(checked, 2, 2, 2, 2));
        NinePatchDrawable nDFocused = new NinePatchDrawable(new NinePatch(focused, 2, 2, 2, 2));
        NinePatchDrawable nDUp = new NinePatchDrawable(new NinePatch(up, 2, 2, 2, 2));
        NinePatchDrawable nBorder0 = new NinePatchDrawable(new NinePatch(border0, 1, 1, 1, 1));
        SolidColorDrawable back_05 = new SolidColorDrawable(new Color(0, 0, 0, 0.5f));
        SolidColorDrawable white = new SolidColorDrawable(new Color(1, 1, 1, 1));

        /// Fonts
        // BitmapFont font16 = skin.get(DynamicFontManager.class).getFont(FontEnum.BASE_FONT, 16);

        LazyBitmapFont lazyBitmapFont = skin.get(LazyBitmapFont.class);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(nDUp, nDChecked, nDUp, lazyBitmapFont);
        textButtonStyle.focused = nDFocused;
        skin.add("default", textButtonStyle);

        Label.LabelStyle style = new Label.LabelStyle(lazyBitmapFont, new Color(1f, 1f, 1f, 1f));
        style.background = back_05;
        skin.add("nameLabel", style);

        TextButton.TextButtonStyle switchBtn = new TextButton.TextButtonStyle(back_05, back_05, back_05, lazyBitmapFont);
        skin.add("switch", switchBtn);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = nBorder0;
        progressBarStyle.knobBefore = white;
        skin.add("default", progressBarStyle);

        return skin;
    }

    // private DynamicFontManager loadFontManager() {
    //     DynamicFontManager dynamicFontManager = DynamicFontManager.create();
    //     append(dynamicFontManager);
    //
    //     return dynamicFontManager;
    // }

    public void append(DynamicFontManager dynamicFontManager) {
        List<String> values = GdxText.values();

        String collect = values.stream()
                               .flatMap(e -> Stream.of(e.toCharArray()))
                               .map(String::new)
                               .distinct()
                               .collect(Collectors.joining());

        if (collect.isEmpty()) {
            return;
        }

        dynamicFontManager.append(FontEnum.BASE_FONT, null, collect);

        // FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        //
        // FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // parameter.size = size;
        // parameter.characters = collect;
        // parameter.minFilter = Texture.TextureFilter.Linear;
        // parameter.magFilter = Texture.TextureFilter.Linear;
        // parameter.hinting = FreeTypeFontGenerator.Hinting.Full; // 启用全提示
        //
        // BitmapFont font = generator.generateFont(parameter);
        // generator.dispose();
        //
        // return font;
    }

    // public void loadMainMenu() {
    //     mainMenu = new MainMenu(skin, assetManager);
    //     mainMenu.act();
    //     mainMenu.getPlayGameSetting().getPlayGame().addListener(new ClickListener() {
    //         @Override
    //         public void clicked(InputEvent event, float x, float y) {
    //             playGameClick();
    //         }
    //     });
    //     GameSingleData.inputBridge.add(mainMenu);
    // }

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
        // gameGenerateData.assetManager = assetManager;
        // gameGenerateData.skin = skin;
        // gameGenerateData.ai = ai;
        asyncExecutor.submit((gameGenerateProcessor = new GameGenerateProcessor(gameGenerateData)));

        // GameSingleData.loadingType = LoadingType.GamePlayLoading;
        // GameSingleData.gamePageStatus = GamePageStatus.Loading;
    }

    public void loadGUI() {
        gui = new GameUI(skin);
        GameSingleData.inputBridge.add(gui);
    }

    public void gamePlayLoading(float delta) {
        if (gameGenerateProcessor != null && !gameGenerateProcessor.update(17)) {
            loading.setProgressValue(gameGenerateProcessor.getProgress());
        } else if (gameGenerateProcessor != null) {
            this.save = gameGenerateProcessor.getSave();
            this.save.getScene().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.save.getScene().add(camera, Groups.CAMERA);
            // this.ai.setScene(this.save.getScene());
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

    // public void menuRender(float delta) {
    //     mainMenu.logic(delta);
    //     mainMenu.render();
    // }

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
        GameSingleData.mrAssetManager = MrAssetManager.create(loadJson(Gdx.files.internal("assets.json")));

        // 界面状态为加载界面
        // GameSingleData.loadingType = LoadingType.ApplicationRunnerLoading;
        // GameSingleData.gamePageStatus = GamePageStatus.Loading;
        GameSingleData.inputBridge = new InputBridge();
        GameSingleData.promptService = new PromptService();

        Gdx.input.setInputProcessor(GameSingleData.inputBridge);

        // assetManager = new AssetManager();
        // GameSingleData.assetManagerProxy = new AssetManagerProxy(assetManager);

        // INIConfiguration texture = loadINIConfiguration(Gdx.files.internal("assets.ini"));

        GameSingleData.skinProxy = loadSkin();

        // Set<String> sections = texture.getSections();
        // for (String sectionName : sections) {
        //     if ("linkConfig".equals(sectionName)) {
        //         continue;
        //     }
        //
        //     SubnodeConfiguration section = texture.getSection(sectionName);
        //
        //     section.getKeys().forEachRemaining(key -> {
        //         assetManager.load(key, Texture.class);
        //     });
        // }

        // assetManager.load("TX Tileset Grass.png", Texture.class);
        // assetManager.load("user.png", Texture.class);
        // assetManager.load("default-checked.9.png", Texture.class);
        // assetManager.load("default-focused.9.png", Texture.class);
        // assetManager.load("default-up.9.png", Texture.class);
        // assetManager.load("log.png", Texture.class);
        // assetManager.load("white.png", Texture.class);
        // assetManager.load("left.png", Texture.class);
        // assetManager.load("Water.png", Texture.class);
        // assetManager.load("TX Plant.png", Texture.class);
        // assetManager.load("TX Shadow Plant.png", Texture.class);
        // assetManager.load("border0.png", Texture.class);
        // assetManager.load("select.png", Texture.class);
        // assetManager.load("select-1.png", Texture.class);


        INIConfiguration core = loadINIConfiguration(Gdx.files.internal("core.ini"));

        homePage = loadHomePage(core);
        // homePage.setSkin(skin);
        // homePage.setAssetManager(assetManager);


        this.loading = new Loading();
        this.asyncExecutor = new AsyncExecutor(1);
        this.camera = buildCamera();

        GameSingleData.rootCamZoom = new OrthographicCameraRootCamZoomImpl(camera.getCamera());
        GameSingleData.positionConverter = camera.getPositionConverter();
    }

    @Override
    public void resize(int width, int height) {
        if (loading != null) {
            loading.resize(width, height);
        }

        homePage.resize(width, height);

        // if (mainMenu != null) {
        //     mainMenu.resize(width, height);
        // }

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

        homePage.logic(delta);
        homePage.render();
    }

    @Override
    public void dispose() {
        homePage.dispose();

        // if (assetManager != null) {
        //     assetManager.dispose();
        // }
        if (skin != null) {
            skin.dispose();
        }
        if (loading != null) {
            loading.dispose();
        }
        // if (mainMenu != null) {
        //     mainMenu.dispose();
        // }
        if (gui != null) {
            gui.dispose();
        }
    }

    private JSONObject loadJson(FileHandle internal) {
        String configStr = new String(internal.readBytes(), StandardCharsets.UTF_8);
        return JSONObject.parseObject(configStr);
    }

    private BaseStage loadHomePage(INIConfiguration core) {
        SubnodeConfiguration subnodeConfiguration = core.getSection("core");

        try {
            Class<?> aClass = Class.forName(subnodeConfiguration.get(String.class, "homePage"));
            if (!aClass.isAssignableFrom(BaseStage.class)) {
                throw new RuntimeException("HomePage 加载失败...");
            }

            Constructor<?> constructor = aClass.getConstructor();
            return (BaseStage) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private INIConfiguration loadINIConfiguration(FileHandle coreIni) {
        Configurations configurations = new Configurations();
        try (InputStream read = coreIni.read();
             InputStreamReader inputStreamReader = new InputStreamReader(read, StandardCharsets.UTF_8);) {

            INIConfiguration ini = new INIConfiguration();
            ini.read(inputStreamReader);

            return ini;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException("INI 加载失败...");
        }
    }

    /**
     * 构建相机,相机自带以下组件:
     * <pre>
     *  1：{@link WASDInput}、{@link TransformMove}：WASD移动组件
     *  2：{@link RollerDragInput}：鼠标中间拖动组件
     *  3：{@link TransformZoom}：滚轮缩放组件
     * </pre>
     *
     * @return 相机
     */
    public ViewportNodeOrthographic buildCamera() {
        WASDInput wasdInput = new WASDInput();

        TransformMove transformMove = new TransformMove(wasdInput.getVector2());
        transformMove.setSpeed(UnitUtil.ofM(18));
        transformMove.setIgnoreTimeScale(true);

        float i = 1080f / 1920f;

        float w = UnitUtil.ofM(13);
        float h = w * i;

        ViewportNodeOrthographic camera = new ViewportNodeOrthographic(w, h);

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

}
