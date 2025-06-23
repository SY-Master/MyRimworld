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
import com.symaster.mrd.api.BasePage;
import com.symaster.mrd.api.ChangeListener;
import com.symaster.mrd.api.SkinProxy;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.enums.BridgeInputProcessorEnum;
import com.symaster.mrd.g2d.LazyBitmapFont;
import com.symaster.mrd.g2d.Progress;
import com.symaster.mrd.g2d.ViewportNodeOrthographic;
import com.symaster.mrd.game.GameGenerateProcessor;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.OrthographicCameraRootCamZoomImpl;
import com.symaster.mrd.game.service.PromptService;
import com.symaster.mrd.game.ui.SceneUI;
import com.symaster.mrd.input.BridgeInputProcessor;
import com.symaster.mrd.input.InputBridge;
import com.symaster.mrd.util.NodeUtil;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private BasePage homePage;
    private BasePage viewPage;

    private GameGenerateProcessor gameGenerateProcessor;
    /**
     * 切换页面的事件
     */
    private final ChangeListener changeListener;

    public Main() {
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
    }

    @Override
    public void create() {

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
    }

    @Override
    public void resize(int width, int height) {
        if (viewPage != null) {
            viewPage.resize(width, height);
        }
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
    }

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

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(nDUp, nDChecked, nDUp,
                                                                                    lazyBitmapFont);
        textButtonStyle.focused = nDFocused;
        skin.add("default", textButtonStyle);

        Label.LabelStyle style = new Label.LabelStyle(lazyBitmapFont, new Color(1f, 1f, 1f, 1f));
        style.background = back_05;
        skin.add("nameLabel", style);

        TextButton.TextButtonStyle switchBtn = new TextButton.TextButtonStyle(back_05, back_05, back_05,
                                                                              lazyBitmapFont);
        skin.add("switch", switchBtn);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = nBorder0;
        progressBarStyle.knobBefore = white;
        skin.add("default", progressBarStyle);

        Progress.Style progressStyle = new Progress.Style();


        return skin;
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
