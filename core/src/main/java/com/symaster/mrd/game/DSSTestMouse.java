package com.symaster.mrd.game;

import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.entity.Creature;
import com.symaster.mrd.game.entity.SelectData;
import com.symaster.mrd.game.service.DSS;
import com.symaster.mrd.game.service.DSSVector;
import com.symaster.mrd.util.SceneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yinmiao
 * @since 2025/3/17
 */
public class DSSTestMouse extends InputNode {

    private static final Logger logger = LoggerFactory.getLogger(DSSTestMouse.class);

    private DSS dss;
    private Vector2 cache = new Vector2();

    public void setDss(DSS dss) {
        this.dss = dss;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != 1) {
            return false;
        }

        SelectData selectData = SceneUtil.getSelectData(getScene());
        if (selectData == null) {
            return false;
        }

        cache.set(screenX, screenY);
        GameSingleData.positionConverter.toWorld(cache);

        boolean flag = false;
        for (Node node : selectData.getNodes()) {
            if (node instanceof Creature) {
                Creature nodes = (Creature) node;
                flag = true;

                DSSVector dssVector = new DSSVector();
                dssVector.setX(cache.x - nodes.getPositionX());
                dssVector.setY(cache.y - nodes.getPositionY());
                dss.moveTo(nodes, dssVector);
            }
        }

        return flag;
    }

}
