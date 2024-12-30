package com.symaster.mrd.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.util.ClassUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class MainStageUI extends Stage {

    private Table table;
    private List<FooterMenuContainer> footerMenus;
    private Scene scene;

    public MainStageUI(Skin skin) {
        super(new ScreenViewport());

        this.table = new Table();
        this.footerMenus = new ArrayList<>();

        List<FooterMenu> footerMenus = this.findFooterMenus().stream()
                .sorted(Comparator.comparingInt(FooterMenu::sort))
                .collect(Collectors.toList());

        for (FooterMenu o : footerMenus) {
            TextButton textButton = new TextButton(o.title(), skin);
            FooterMenuContainer m = new FooterMenuContainer(o, textButton);

            this.footerMenus.add(m);
            this.table.add(m.getMenuBtn()).expand().fill();
            if (m.getFooterMenu().panel() != null) {
                m.setMenuActor(new MenuActor(m.getFooterMenu().panel()));
                this.addActor(m.getMenuActor());
                m.getMenuActor().setVisible(false);
            }
        }

        this.addActor(this.table);
    }

    public List<FooterMenu> findFooterMenus() {
        Set<Class<?>> scan = ClassUtil.scan("com.symaster.mrd.gui", FooterMenu.class::isAssignableFrom);
        List<FooterMenu> footerMenus = new ArrayList<>();

        try {
            for (Class<?> aClass : scan) {

                Constructor<?> constructor = aClass.getConstructor();
                FooterMenu o = (FooterMenu) constructor.newInstance();
                footerMenus.add(o);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return footerMenus;
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        table.setSize(width, 50);
    }

    /**
     * 逻辑处理
     *
     * @param delta Time in seconds since the last frame.
     */
    public void logic(float delta) {
        if (scene == null) {
            return;
        }

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
    }

    public void render() {
        if (scene == null) {
            return;
        }

        getViewport().apply();
        draw();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
