package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.Creature;
import com.symaster.mrd.game.entity.Human;
import com.symaster.mrd.game.entity.Race;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public class PartnerPanel extends Group implements Disposable {
    private final PartnerMenu partnerMenu;
    private final Image background;
    private final SolidColorDrawable solidColorDrawable;

    public PartnerPanel(PartnerMenu partnerMenu) {
        this.partnerMenu = partnerMenu;
        this.solidColorDrawable = new SolidColorDrawable(new Color(0, 0, 0, 0.6f));
        this.background = new Image();
        addActor(this.background);
    }

    public PartnerMenu getPartner() {
        return partnerMenu;
    }

    public Image getBackground() {
        return background;
    }

    public void logic(float delta) {
        background.setSize(getWidth(), getHeight());

        if (!isVisible() || partnerMenu.getMainStageUI() == null || partnerMenu.getMainStageUI().getScene() == null) {
            return;
        }

        Set<Node> byGroup = partnerMenu.getMainStageUI().getScene().getByGroup(Groups.PARTNER);
        if (byGroup != null) {
            List<Creature> creatures = byGroup.stream()
                    .filter(e -> e instanceof Creature)
                    .map(e -> (Creature) e)
                    .sorted((o1, o2) -> {
                        if (o1.getRace() == o2.getRace()) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }

                        return Integer.compare(o1.getRace().ordinal(), o2.getRace().ordinal());
                    }).collect(Collectors.toList());

            for (Creature node : creatures) {

            }
        }
    }

    @Override
    public void dispose() {
        solidColorDrawable.dispose();
    }
}
