package com.symaster.mrd.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.ui.footermenu.BuildingMenu;
import com.symaster.mrd.game.ui.footermenu.CreatureMenu;
import com.symaster.mrd.game.ui.footermenu.PartnerMenu;
import com.symaster.mrd.game.ui.footermenu.Setting;
import com.symaster.mrd.gui.*;
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

    private final Table table;
    private final List<MainStageUIItem> footerMenus;
    private Scene scene;
    private final Skin skin;

    public MainStageUI(Skin skin) {
        super(new ScreenViewport());
        this.skin = skin;
        this.table = new Table();
        this.footerMenus = new ArrayList<>();

        addTo(new BuildingMenu());
        addTo(new CreatureMenu());
        addTo(new PartnerMenu());
        addTo(new Setting());

        this.addActor(this.table);
    }

    public Table getTable() {
        return table;
    }

    public Scene getScene() {
        return scene;
    }

    private void addTo(MainStageUIItem o) {
        o.setSkin(skin);
        o.setMainStageUI(this);
        o.create();
        o.addPanelOpenListener((item) -> {
            if (item.panel() == null) {
                return;
            }
            item.panel().setVisible(!item.panel().isVisible());
        });

        this.footerMenus.add(o);
        if (o.layoutConfig() != null && BTNPosition.BottomMenu == o.layoutConfig().btnPosition()) {
            this.table.add(o.key()).expand().fill();
        }

        if (o.panel() != null) {
            this.addActor(o.panel());
            o.panel().setVisible(false);
        }
    }

    public List<MainStageUIItem> findFooterMenus() {
        Set<Class<?>> scan = ClassUtil.scan("com.symaster.mrd.gui", MainStageUIItem.class::isAssignableFrom);
        List<MainStageUIItem> mainStageUIItems = new ArrayList<>();

        try {
            for (Class<?> aClass : scan) {

                Constructor<?> constructor = aClass.getConstructor();
                MainStageUIItem o = (MainStageUIItem) constructor.newInstance();
                mainStageUIItems.add(o);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return mainStageUIItems;
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);

        final int bottomMenuHeight = 50;
        int avaHeight = height - bottomMenuHeight;
        table.setSize(width, bottomMenuHeight);

        for (MainStageUIItem footerMenu : footerMenus) {
            Actor panel = footerMenu.panel();
            LayoutConfig layoutConfig = footerMenu.layoutConfig();

            if (panel == null || layoutConfig == null) {
                continue;
            }

            int panelWidth = layoutConfig.panelWidth(width);
            int panelHeight = layoutConfig.panelHeight(height);

            UIPosition uiPosition = layoutConfig.uiPosition();
            if (UIPosition.LEFT_DOWN == uiPosition) {
                panel.setSize(panelWidth, Math.min(avaHeight, panelHeight));
                panel.setPosition(0, bottomMenuHeight);
            }


        }
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

        for (MainStageUIItem footerMenu : footerMenus) {
            footerMenu.logic(delta);
        }

        // for (FooterMenuContainer footerMenu : footerMenus) {
        //     if (footerMenu.getMenuActor() != null && footerMenu.getMenuBtn().isChecked()) {
        //         footerMenu.getMenuBtn().toggle();
        //
        //         for (FooterMenuContainer menu : footerMenus) {
        //             MenuActor menuActor = menu.getMenuActor();
        //             if (menuActor.isVisible()) {
        //                 menuActor.setVisible(false);
        //             }
        //         }
        //
        //         MenuActor menuActor = footerMenu.getMenuActor();
        //         menuActor.setVisible(true);
        //     }
        // }
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
