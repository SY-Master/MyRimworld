package com.symaster.mrd.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.util.ClassUtil;

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

    public static MainStageUI create(Skin skin) {
        MainStageUI mainStageUI = new MainStageUI();

        mainStageUI.table = new Table();
        mainStageUI.footerMenus = new ArrayList<>();

        Set<Class<?>> scan = ClassUtil.scan("com.symaster.mrd.gui", FooterMenu.class::isAssignableFrom);

        try {
            for (Class<?> aClass : scan) {

                Constructor<?> constructor = aClass.getConstructor();
                FooterMenu o = (FooterMenu) constructor.newInstance();

                TextButton textButton = new TextButton(o.title(), skin);
                FooterMenuContainer m = new FooterMenuContainer(o, textButton);

                mainStageUI.footerMenus.add(m);
                mainStageUI.table.add(m.getMenuBtn()).expand().fill();
                if (m.getFooterMenu().panel() != null) {
                    m.setMenuActor(new MenuActor(m.getFooterMenu().panel()));
                    mainStageUI.addActor(m.getMenuActor());
                    m.getMenuActor().setVisible(false);
                }
            }
        } catch (NoSuchMethodException |InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        mainStageUI.addActor(mainStageUI.table);

        return mainStageUI;
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        table.setSize(width, 50);
    }

    public void render() {

        for (FooterMenuContainer footerMenu : footerMenus) {
            if (footerMenu.getMenuActor() != null && footerMenu.getMenuBtn().isChecked()) {
                footerMenu.getMenuBtn().toggle();

                for (FooterMenuContainer menu : footerMenus) {
                    MenuActor menuActor = menu.getMenuActor();
                    if (menuActor.isVisible()) {
                        menuActor.setVisible(false);
                    }
                }

                MenuActor menuActor = footerMenu.getMenuActor();
                menuActor.setVisible(true);
            }
        }

        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        act(delta);
        draw();
    }
}
