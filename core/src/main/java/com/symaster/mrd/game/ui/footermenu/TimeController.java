package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.game.ui.GameUIItem;
import com.symaster.mrd.gui.UIPosition;

/**
 * 时间控制器
 *
 * @author yinmiao
 * @since 2025/1/1
 */
public class TimeController extends GameUIItem {

    private Group group;
    private TextButton pause;
    private TextButton play0_5;
    private TextButton play;
    private TextButton play2;
    private TextButton play4;
    private TextButton play6;
    private TextButton play10;

    @Override
    public void create() {
        super.create();
        setPanelNormallyOpen(true);

        group = new Group();

        pause = new TextButton("暂停", getSkin().get("switch", TextButton.TextButtonStyle.class));
        pause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SystemConfig.TIME_SCALE = 0f;
            }
        });
        group.addActor(pause);

        play0_5 = new TextButton("0.5", getSkin().get("switch", TextButton.TextButtonStyle.class));
        play0_5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SystemConfig.TIME_SCALE = 0.5f;
            }
        });
        group.addActor(play0_5);

        play = new TextButton("常速", getSkin().get("switch", TextButton.TextButtonStyle.class));
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SystemConfig.TIME_SCALE = 1f;
            }
        });
        group.addActor(play);

        play2 = new TextButton("X2", getSkin().get("switch", TextButton.TextButtonStyle.class));
        play2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SystemConfig.TIME_SCALE = 2f;
            }
        });
        group.addActor(play2);

        play4 = new TextButton("X4", getSkin().get("switch", TextButton.TextButtonStyle.class));
        play4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SystemConfig.TIME_SCALE = 4f;
            }
        });
        group.addActor(play4);

        play6 = new TextButton("X6", getSkin().get("switch", TextButton.TextButtonStyle.class));
        play6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SystemConfig.TIME_SCALE = 6f;
            }
        });
        group.addActor(play6);

        play10 = new TextButton("X30", getSkin().get("switch", TextButton.TextButtonStyle.class));
        play10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SystemConfig.TIME_SCALE = 30f;
            }
        });
        group.addActor(play10);


    }

    @Override
    public Actor key() {
        return null;
    }

    @Override
    public Actor panel() {
        return group;
    }

    @Override
    public UIPosition uiPosition() {
        return UIPosition.RIGHT_DOWN;
    }

    @Override
    public int panelWidth(int sceneWidth, int sceneHeight) {
        return 300;
    }

    @Override
    public int panelHeight(int sceneWidth, int sceneHeight) {
        return 30;
    }

    @Override
    public void logic(float delta) {
        super.logic(delta);

        float width = group.getWidth() / 7;
        pause.setSize(width, 30);

        play0_5.setSize(width, 30);
        play0_5.setPosition(width, 0);

        play.setSize(width, 30);
        play.setPosition(width * 2, 0);

        play2.setSize(width, 30);
        play2.setPosition(width * 3, 0);

        play4.setSize(width, 30);
        play4.setPosition(width * 4, 0);

        play6.setSize(width, 30);
        play6.setPosition(width * 5, 0);

        play10.setSize(width, 30);
        play10.setPosition(width * 6, 0);
    }

}
