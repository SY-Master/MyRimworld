package com.symaster.mrd.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.util.ClassUtil;
import com.symaster.mrd.util.GdxText;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class MainStageUI extends Stage {

    private Table table;
    private List<FooterMenuContainer> footerMenus;

    public MainStageUI() {
        super(new ScreenViewport());
    }

    public static MainStageUI create(BitmapFont font) {
        MainStageUI mainStageUI = new MainStageUI();

        SolidColorDrawable Drawable = new SolidColorDrawable(Color.DARK_GRAY);
        SolidColorDrawable Drawable1 = new SolidColorDrawable(Color.GRAY);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(Drawable, Drawable1, Drawable, font);

        mainStageUI.table = new Table();
        mainStageUI.footerMenus = new ArrayList<>();

        Set<Class<?>> scan = ClassUtil.scan("com.symaster.mrd.gui", FooterMenu.class::isAssignableFrom);

        try {
            for (Class<?> aClass : scan) {

                Constructor<?> constructor = aClass.getConstructor();
                FooterMenu o = (FooterMenu) constructor.newInstance();

                FooterMenuContainer m = new FooterMenuContainer(o, new TextButton(o.title(), style));

                mainStageUI.footerMenus.add(m);
                mainStageUI.table.add(m.getMenuBtn()).expand().fill();
            }
        } catch (NoSuchMethodException |InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // 将标签添加到舞台
        mainStageUI.addActor(mainStageUI.table);

        return mainStageUI;
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        table.setSize(width, 50);
    }

    public void render() {

        for (FooterMenuContainer footerMenu : footerMenus) {
            if (footerMenu.getMenuBtn().isChecked()) {
                System.out.println("check");
                footerMenu.getMenuBtn().toggle();
            }
        }

        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        act(delta);
        draw();
    }
}
