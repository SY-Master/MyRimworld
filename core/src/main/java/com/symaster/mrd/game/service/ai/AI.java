package com.symaster.mrd.game.service.ai;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.Creature;
import com.symaster.mrd.game.entity.Race;

import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/30
 */
public class AI {

    private Scene scene;
    private AIData aiData;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        Set<Node> byGroup = this.scene.getByGroup(Groups.AI_DATA);
        if (byGroup == null) {
            this.scene.add(new AIData(), Groups.AI_DATA);
        }

        aiData = (AIData) this.scene.getByGroup(Groups.AI_DATA).iterator().next();
    }

    public void logic(Creature nodes, float delta) {
        if (scene == null || aiData == null) {
            return;
        }

        if (nodes.getRace() == Race.Human) {
            // 人类的AI
            human(nodes, delta);
        }

    }

    private void human(Creature nodes, float delta) {
        //   - 工作：工作区间，理论上是工作，但是当没有任何工作的时间会执行其他事情，工作也分为了两种：命令、普通工作。行为顺序如下：
        //     1. 吃东西：最高优先级。当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
        //     2. 命令工作：只有吃饱后才有力气干活。
        //     3. 普通工作：有工作时，会自动工作。
        //     4. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
        //     5. 娱乐：概率发生，随机和各种娱乐设施互动。
        //     6. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
        //     7. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。
        //
        //   - 娱乐：娱乐时间段，生物不会干活。行为顺序如下：
        //     1. 吃东西：最高优先级。当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
        //     2. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
        //     3. 娱乐：概率发生，较大概率，随机和各种娱乐设施互动。
        //     4. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
        //     5. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。
        //
        //   - 自由活动：自由活动区间比较难定义。理论上所有行为都可能发生。
        //     1. 吃东西：当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
        //     2. 睡觉：当休息值小于20%时会睡觉。
        //     3. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
        //     4. 娱乐：概率发生，随机和各种娱乐设施互动。
        //     5. 命令工作：有命令工作时，会执行相应的命令。
        //     6. 普通工作：有工作时，会自动工作。
        //     7. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
        //     8. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。
        // - 行为设定：
        //   - 吃东西：生物会自动寻找并吃掉相应的食物，一直到饱食度达到100%。
        //   - 互动：生物会主动根据策略寻找附近的生物互动，互动类型定义如下：
        //     - 打招呼：生物会找到目标生物，然后移动到目标生物身边，目标生物要求如下：
        //       - 不处于睡觉行为下


    }
}
