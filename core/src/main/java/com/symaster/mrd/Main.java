package com.symaster.mrd;

import com.alibaba.fastjson2.JSONObject;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.symaster.mrd.api.BasePage;
import com.symaster.mrd.api.ChangeListener;
import com.symaster.mrd.api.SkinProxy;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.enums.BridgeInputProcessorEnum;
import com.symaster.mrd.g2d.*;
import com.symaster.mrd.game.GameGenerateProcessor;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.OrthographicCameraRootCamZoomImpl;
import com.symaster.mrd.game.service.PromptService;
import com.symaster.mrd.game.ui.SceneUI;
import com.symaster.mrd.input.BridgeInputProcessor;
import com.symaster.mrd.input.InputBridge;
import com.symaster.mrd.util.GdxText;
import com.symaster.mrd.util.NodeUtil;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    // private Save save;
    // private SceneUI gui;

    private BasePage homePage;
    private BasePage viewPage;

    // private Skin skin;
    // private Loading loading;
    private GameGenerateProcessor gameGenerateProcessor;
    private AsyncExecutor asyncExecutor;
    private ChangeListener changeListener;
    // private ViewportNodeOrthographic camera;

    public SkinProxy loadSkin() {
        Skin skin1 = buildSkin();
        return new SkinProxy(skin1);
    }

    /**
     * 构建 Skin。本项目里面Skin主要用户GUI的皮肤
     *
     * @return Skin
     */
    public Skin buildSkin() {
        Skin skin = new Skin();

        FileHandle internal = Gdx.files.internal("fonts/font.ini");

        INIConfiguration iniConfiguration = loadINIConfiguration(internal);
        String baseFontPath = iniConfiguration.getSection("fontMap").get(String.class, "baseFont");
        FileHandle internal1 = Gdx.files.internal("fonts/" + baseFontPath);

        skin.add("default", new LazyBitmapFont(new FreeTypeFontGenerator(internal1), 16));
        skin.add("18", new LazyBitmapFont(new FreeTypeFontGenerator(internal1), 18));
        skin.add("20", new LazyBitmapFont(new FreeTypeFontGenerator(internal1), 20));
        skin.add("22", new LazyBitmapFont(new FreeTypeFontGenerator(internal1), 22));
        skin.add("default", new Color(1f, 1f, 1f, 1f));

        // GameSingleData.mrAssetManager.getGlobal("")
        Texture checked = GameSingleData.mrAssetManager.getGlobal("checked", Texture.class);
        Texture focused = GameSingleData.mrAssetManager.getGlobal("focused", Texture.class);
        Texture up = GameSingleData.mrAssetManager.getGlobal("up", Texture.class);
        Texture border0 = GameSingleData.mrAssetManager.getGlobal("border", Texture.class);

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

        Progress.Style progressStyle = new Progress.Style();


        return skin;
    }

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

    // /**
    //  * 开始游戏
    //  */
    // public void playGameClick() {
    //     if (save != null) {
    //         save.dispose();
    //         save = null;
    //     }
    //
    //     GameGenerateData gameGenerateData = new GameGenerateData();
    //     gameGenerateData.mapSeed = UUID.randomUUID().toString();
    //     // gameGenerateData.assetManager = assetManager;
    //     // gameGenerateData.skin = skin;
    //     // gameGenerateData.ai = ai;
    //     asyncExecutor.submit((gameGenerateProcessor = new GameGenerateProcessor(gameGenerateData)));
    //
    //     // GameSingleData.loadingType = LoadingType.GamePlayLoading;
    //     // GameSingleData.gamePageStatus = GamePageStatus.Loading;
    // }

    // public void loadGUI() {
    //     gui = new SceneUI();
    //     gui.created();
    // }

    // public void gamePlayLoading(float delta) {
    //     if (gameGenerateProcessor != null && !gameGenerateProcessor.update(17)) {
    //         loading.setProgressValue(gameGenerateProcessor.getProgress());
    //     } else if (gameGenerateProcessor != null) {
    //         this.save = gameGenerateProcessor.getSave();
    //         this.save.getScene().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //         this.save.getScene().add(camera, Groups.CAMERA);
    //         // this.ai.setScene(this.save.getScene());
    //         this.gui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //         this.gui.setScene(save.getScene());
    //
    //         this.loading.setProgressValue(1f);
    //         this.loading.logic(delta);
    //         this.loading.render();
    //
    //         GameSingleData.gamePageStatus = GamePageStatus.Game;
    //     }
    //
    //     loading.logic(delta);
    //     loading.render();
    // }

    // public void gameRender(float delta) {
    //     // 处理场景的逻辑
    //     save.getScene().logic(delta);
    //     // 处理GUI的逻辑
    //     gui.logic(delta);
    //
    //     // 绘制相机
    //     camera.render();
    //     // 绘制GUI
    //     gui.render();
    // }

    @Override
    public void create() {
        changeListener = new ChangeListener() {
            @Override
            public void change(BasePage oldPage, BasePage newPage) {
                for (BridgeInputProcessor inputProcessor : oldPage.getInputProcessors()) {
                    GameSingleData.inputBridge.remove(inputProcessor);
                }
                for (BridgeInputProcessor inputProcessor : newPage.getInputProcessors()) {
                    GameSingleData.inputBridge.add(inputProcessor);
                }
                viewPage = newPage;

                update(oldPage, newPage);
            }

            @Override
            public void toHome(BasePage oldPage) {
                viewPage = homePage;
                update(oldPage, homePage);
            }

            private void update(BasePage oldPage, BasePage newPage) {
                oldPage.removeChangeListener(Main.this.changeListener);
                newPage.addChangeListener(Main.this.changeListener);
            }
        };

        // 全局事件处理器
        GameSingleData.inputBridge = new InputBridge();
        Gdx.input.setInputProcessor(GameSingleData.inputBridge);
        GameSingleData.inputBridge.enableGroup(BridgeInputProcessorEnum.PAGE.getCode());

        // 全局资源管理器
        GameSingleData.mrAssetManager = MrAssetManager.create(loadJson(Gdx.files.internal("assets.json")));

        GameSingleData.promptService = new PromptService();

        // 加载全局GUI皮肤
        GameSingleData.skinProxy = loadSkin();

        JSONObject coreJson = loadJson(Gdx.files.internal("core.json"));

        this.homePage = loadHomePage(coreJson);
        viewPage = this.homePage;
        this.homePage.addChangeListener(changeListener);

        for (BridgeInputProcessor inputProcessor : this.homePage.getInputProcessors()) {
            GameSingleData.inputBridge.add(inputProcessor);
        }

        GameSingleData.camera = defaultCamera(coreJson);
        GameSingleData.sceneUI = new SceneUI();
        GameSingleData.sceneUI.created();
        GameSingleData.rootCamZoom = new OrthographicCameraRootCamZoomImpl(GameSingleData.camera.getCamera());
        GameSingleData.positionConverter = GameSingleData.camera.getPositionConverter();

        // this.camera = defaultCamera(coreJson);
        // loadGUI();
    }

    @Override
    public void resize(int width, int height) {
        // if (loading != null) {
        //     loading.resize(width, height);
        // }

        // if (homePage != null) {
        //     homePage.resize(width, height);
        // }

        // if (gui != null) {
        //     gui.resize(width, height);
        // }

        // if (save != null && save.getScene() != null) {
        //     save.getScene().resize(width, height);
        // }

        // if (camera != null) {
        //     camera.getViewport().update(width, height);
        // }

        if (viewPage != null) {
            viewPage.resize(width, height);
        }

        // fillViewport.getViewport().update(width, height);
        // gui.resize(width, height);
        // scene.resize(width, height);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (viewPage != null) {
            viewPage.logic(delta);
            viewPage.render();
        }
    }

    @Override
    public void dispose() {
        homePage.dispose();

        if (viewPage != null) {
            viewPage.dispose();
        }

        // if (assetManager != null) {
        //     assetManager.dispose();
        // }
        // if (skin != null) {
        //     skin.dispose();
        // }
        // if (loading != null) {
        //     loading.dispose();
        // }
        // if (mainMenu != null) {
        //     mainMenu.dispose();
        // }
        // if (gui != null) {
        //     gui.dispose();
        // }
    }

    private JSONObject loadJson(FileHandle internal) {
        String configStr = new String(internal.readBytes(), StandardCharsets.UTF_8);
        return JSONObject.parseObject(configStr);
    }

    private BasePage loadHomePage(JSONObject coreJson) {
        JSONObject core = coreJson.getJSONObject("core");

        JSONObject defaultHomePageJson = core.getJSONObject("defaultHomePage");

        if (defaultHomePageJson == null) {
            throw new IllegalArgumentException("defaultHomePage Is Empty");
        }

        String type = defaultHomePageJson.getString("type");

        try {
            Class<?> aClass = Class.forName(type);

            if (!BasePage.class.isAssignableFrom(aClass)) {
                throw new RuntimeException("HomePage 加载失败...");
            }

            Constructor<?> constructor = aClass.getConstructor();
            BasePage baseStage = (BasePage) constructor.newInstance();

            baseStage.created();

            return baseStage;
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

    public ViewportNodeOrthographic defaultCamera(JSONObject coreJson) {

        JSONObject nodes = coreJson.getJSONObject("nodes");

        JSONObject defaultCameraJson = nodes.getJSONObject("defaultCamera");

        try {
            return NodeUtil.createNode(defaultCameraJson, ViewportNodeOrthographic.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // WASDInput wasdInput = new WASDInput();
        // wasdInput.setTransformInput(new TransformInput());
        //
        // TransformMove transformMove = new TransformMove();
        // transformMove.setSpeed(UnitUtil.ofM(18));
        // transformMove.setIgnoreTimeScale(true);
        //
        // float i = 1080f / 1920f;
        //
        // float w = UnitUtil.ofM(13);
        // float h = w * i;
        //
        // ViewportNodeOrthographic camera = new ViewportNodeOrthographic(w, h);
        //
        // RollerDragInput rollerDragInput = new RollerDragInput(camera);
        //
        // camera.add(rollerDragInput);
        // camera.add(wasdInput);
        // camera.add(transformMove);
        //
        // TransformZoom nodes = new TransformZoom(camera.getCamera(), camera) {
        //     @Override
        //     public boolean scrolled(float amountX, float amountY) {
        //         boolean scrolled = super.scrolled(amountX, amountY);
        //
        //         // 相机越高，移动越快
        //         if (camera.getWorldRectangle().getWidth() > 0 && scrolled) {
        //             transformMove.setSpeed(camera.getWorldRectangle().getWidth() * 0.25f);
        //         }
        //
        //         return scrolled;
        //     }
        // };
        //
        // camera.add(nodes);
        // return camera;
    }

}
