package com.symaster.mrd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.util.GdxText;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private Stage stage;
    private Skin skin;

    @Override
    public void create() {
        BitmapFont font = textLoad(SystemConfig.TEXT_PATH, SystemConfig.FONT_PATH, SystemConfig.FONT_SIZE);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/def.ttf"));
        // FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // parameter.size = 14; // 设置字体大小
        // parameter.characters = " 你好世界!，aAbB"; // 包含你需要的字符集，特别是中文字符
        //
        // BitmapFont font = generator.generateFont(parameter);
        // generator.dispose(); // 处理完后释放生成器资源


        // TiledDrawable tiledDrawable = new TiledDrawable();
        SolidColorDrawable Drawable = new SolidColorDrawable(Color.DARK_GRAY);
        SolidColorDrawable Drawable1 = new SolidColorDrawable(Color.GRAY);
        SolidColorDrawable Drawable2 = new SolidColorDrawable(Color.BLACK);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(Drawable, Drawable1, Drawable2, font);
        TextButton btn = new TextButton(GdxText.get("e98a245f-3f19-4132-a925-e63a44852f69"), style);

        // 创建一个 LabelStyle 并应用字体
        // Label.LabelStyle labelStyle = new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE);

        // 使用 LabelStyle 创建一个 Label
        // Label label = new Label("你好，世界界界界界! aAbB", labelStyle);
        // label.setPosition(50, 100); // 设置位置

        // 将标签添加到舞台
        stage.addActor(btn);
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
        parameter.size = size; // 设置字体大小
        parameter.characters = collect; // 包含你需要的字符集，特别是中文字符

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // 处理完后释放生成器资源

        return font;
    }

    private void cleanCrispy() {
        FileHandle atlasFile = Gdx.files.internal("gdx-skins-master/clean-crispy/skin/clean-crispy-ui.atlas");
        FileHandle jsonFile = Gdx.files.internal("gdx-skins-master/clean-crispy/skin/clean-crispy-ui.json");
        skin = new Skin(jsonFile, new TextureAtlas(atlasFile));
    }

    private void biologicalAttack() {
        FileHandle atlasFile = Gdx.files.internal("gdx-skins-master/biological-attack/skin/biological-attack-ui.atlas");
        FileHandle jsonFile = Gdx.files.internal("gdx-skins-master/biological-attack/skin/biological-attack-ui.json");
        skin = new Skin(jsonFile, new TextureAtlas(atlasFile));
    }

    private void arcade() {
        FileHandle atlasFile = Gdx.files.internal("gdx-skins-master/arcade/skin/arcade-ui.atlas");
        FileHandle jsonFile = Gdx.files.internal("gdx-skins-master/arcade/skin/arcade-ui.json");
        skin = new Skin(jsonFile, new TextureAtlas(atlasFile));
    }

    @Override
    public void resize(int width, int height) {
        // See below for what true means.
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();

        if (skin != null) {
            skin.dispose();
        }
    }
}