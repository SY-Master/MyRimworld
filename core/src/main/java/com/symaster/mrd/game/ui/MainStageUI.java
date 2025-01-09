package com.symaster.mrd.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.GamePageStatus;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.UILayer;
import com.symaster.mrd.game.ui.footermenu.*;
import com.symaster.mrd.gui.BTNPosition;
import com.symaster.mrd.gui.UIPosition;
import com.symaster.mrd.input.BridgeInputProcessor;
import com.symaster.mrd.util.ClassUtil;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class MainStageUI extends Stage implements BridgeInputProcessor {

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
        addTo(new TimeView());
        addTo(new TimeController());
        addTo(new CameraZoomView());

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
        if (!o.isPanelNormallyOpen()) {
            o.addPanelOpenListener((item) -> {
                if (item.panel() == null) {
                    return;
                }
                item.panel().setVisible(!item.panel().isVisible());
            });
        }

        this.footerMenus.add(o);
        if (BTNPosition.BottomMenu == o.btnPosition()) {
            this.table.add(o.key()).expand().fill();
        }

        if (o.panel() != null) {
            this.addActor(o.panel());
            o.panel().setVisible(o.isPanelNormallyOpen());
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

        // 底部菜单高度
        final int bottomMenuHeight = 50;

        // 屏幕可用高度
        int avaHeight = height - bottomMenuHeight;

        // 底部菜单尺寸设置
        table.setSize(width, bottomMenuHeight);

        // 面板布局与尺寸设置
        for (MainStageUIItem footerMenu : footerMenus) {

            // 面板
            Actor panel = footerMenu.panel();
            // 面板位置
            UIPosition uiPosition = footerMenu.uiPosition();

            if (panel == null || uiPosition == null) {
                continue;
            }

            // 面板宽度
            int panelWidth = footerMenu.panelWidth(width, height);
            // 面板高度
            int panelHeight = Math.min(avaHeight, footerMenu.panelHeight(width, height));

            // 面板边距
            Insets marge = footerMenu.marge(width, height);

            // 设置面板尺寸
            panel.setSize(panelWidth, panelHeight);

            // 靠下边距
            int down = bottomMenuHeight + marge.bottom;
            // 靠上边距
            int up = height - panelHeight - marge.top;
            // 靠左边距
            int left = marge.left;
            // 靠右边距
            int right = width - panelWidth - marge.right;

            if (UIPosition.LEFT_DOWN == uiPosition) {
                // 左下角
                panel.setPosition(left, down);
            } else if (UIPosition.LEFT_UP == uiPosition) {
                // 左上角
                panel.setPosition(left, up);
            } else if (UIPosition.RIGHT_DOWN == uiPosition) {
                // 右下角
                panel.setPosition(right, down);
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

    @Override
    public void dispose() {
        super.dispose();

        for (MainStageUIItem footerMenu : footerMenus) {
            footerMenu.dispose();
        }
    }

    @Override
    public int uiLayer() {
        return UILayer.Gui.getLayer();
    }

    @Override
    public int uiSort() {
        if (GameSingleData.gamePageStatus == GamePageStatus.Game) {
            return 0;
        } else {
            return 99;
        }
    }

    /**
     * @return 是否启用输入事件
     */
    @Override
    public boolean actionEnable() {
        return GameSingleData.gamePageStatus == GamePageStatus.Game;
    }
}
