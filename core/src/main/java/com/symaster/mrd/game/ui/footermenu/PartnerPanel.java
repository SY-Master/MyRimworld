package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.Human;

import java.util.List;
import java.util.*;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public class PartnerPanel extends Group implements Disposable {
    private final PartnerMenu partnerMenu;
    private final Image background;
    private final SolidColorDrawable solidColorDrawable;
    private final Map<Node, Item> items;
    private final List<Node> deletes;
    private final Skin skin;
    private final VerticalGroup list;
    private final ScrollPane scrollPane;

    public PartnerPanel(PartnerMenu partnerMenu) {
        this.partnerMenu = partnerMenu;
        skin = partnerMenu.getSkin();
        this.solidColorDrawable = new SolidColorDrawable(new Color(0.1f, 0.1f, 0.1f, 0.8f));
        this.background = new Image(this.solidColorDrawable);
        background.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                return true;
            }
        });
        this.items = new HashMap<>();
        this.deletes = new LinkedList<>();

        list = new VerticalGroup();
        scrollPane = new ScrollPane(list);

        addActor(this.background);
        addActor(this.scrollPane);
    }

    public PartnerMenu getPartner() {
        return partnerMenu;
    }

    public Image getBackground() {
        return background;
    }

    public void logic(float delta) {
        if (!isVisible() || partnerMenu.getMainStageUI() == null || partnerMenu.getMainStageUI().getScene() == null) {
            return;
        }

        background.setSize(getWidth(), getHeight());
        scrollPane.setSize(getWidth(), getHeight());

        Set<Node> byGroup = partnerMenu.getMainStageUI().getScene().getByGroup(Groups.PARTNER);
        if (byGroup != null) {
            for (Node node : byGroup) {
                if (!items.containsKey(node)) {
                    Item item = new Item(node, Math.round(getWidth()) - 10, 40, this);
                    items.put(node, item);
                    list.addActor(item);
                }
            }
        }

        for (Node node : items.keySet()) {
            if (byGroup == null || !byGroup.contains(node)) {
                deletes.add(node);
            }
        }

        for (Node delete : deletes) {
            Item item = items.get(delete);
            list.removeActor(item);
            items.remove(delete);
        }

        for (Item value : items.values()) {
            value.logic(delta);
        }

        // Array<Action> actions = list.getActions();

    }

    @Override
    public void dispose() {
        solidColorDrawable.dispose();
    }

    private static class Item extends Group {
        private final Node node;
        private final Label label;

        private Item(Node node, int w, int h, PartnerPanel partnerPanel) {
            this.node = node;
            setSize(w, h);
            label = new Label("", partnerPanel.skin.get("nameLabel", Label.LabelStyle.class));
            addActor(label);
        }

        public void logic(float delta) {
            if (node instanceof Human) {
                label.setText(((Human) node).getName());
            }
        }
    }
}
