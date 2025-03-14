package com.symaster.mrd.game.service.ai;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.Creature;
import com.symaster.mrd.game.entity.Database;
import com.symaster.mrd.game.entity.GameTime;
import com.symaster.mrd.game.entity.Race;

import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/30
 */
public class AI {

    private Scene scene;
    private Database database;
    private GameTime gameTime;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        Set<Node> byGroup = this.scene.getByGroup(Groups.DATABASE);
        if (byGroup == null) {
            this.scene.add(new Database(), Groups.DATABASE);
        }

        database = (Database) this.scene.getByGroup(Groups.DATABASE).iterator().next();

        Set<Node> byGroup1 = scene.getByGroup(Groups.TIMER);
        if (byGroup1 != null) {
            gameTime = (GameTime) byGroup1.iterator().next();
        }
    }

    public void logic(Creature nodes, float delta) {
        if (scene == null || database == null) {
            return;
        }

        if (nodes.getRace() == Race.Human) {
            // 人类的AI
            human(nodes, delta);
        }
    }

    private void human(Creature nodes, float delta) {
        if (gameTime == null || database == null) {
            return;
        }

        HumanAction humanAction1 = database.getHumanAction(nodes.getId());
        if (humanAction1 != null) {
            humanAction1.logic(scene, database, gameTime, nodes, delta);
            return;
        }

        // 时间分配设定
        TimeAllocation timeAllocation;
        if (database.getTimeAllocation(gameTime.getHour()) == null) { // null 为未定义（自由活动）
            timeAllocation = TimeAllocation.FreeActivities;
        } else {
            timeAllocation = database.getTimeAllocation(gameTime.getHour());
        }

        // 当前时间分配
        HumanAction[] humanAction = timeAllocation.getHumanAction();

        HumanAction action = null;
        for (HumanAction actionFor : humanAction) {
            if (actionFor.available(scene, database, gameTime, nodes, delta)) {
                action = actionFor;
                break;
            }
        }

        if (action != null) {
            action.logic(scene, database, gameTime, nodes, delta);
        }
    }

}
