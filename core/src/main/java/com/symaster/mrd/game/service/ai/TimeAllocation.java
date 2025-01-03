package com.symaster.mrd.game.service.ai;

/**
 * 时间分配
 *
 * @author yinmiao
 * @since 2025/1/1
 */
public enum TimeAllocation {
    /**
     * 休息
     */
    Rest(HumanAction.Sleep),
    /**
     * 工作
     * 1. 吃东西：最高优先级。当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
     * 2. 命令工作：只有吃饱后才有力气干活。
     * 3. 普通工作：有工作时，会自动工作。
     * 4. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
     * 5. 娱乐：概率发生，随机和各种娱乐设施互动。
     * 6. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
     * 7. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。
     */
    Work(HumanAction.Eating, HumanAction.CommandWork, HumanAction.OrdinaryWork, HumanAction.Interaction, HumanAction.Entertainment, HumanAction.StrollingAround, HumanAction.LostInThought),
    /**
     * 娱乐
     * 1. 吃东西：最高优先级。当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
     * 2. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
     * 3. 娱乐：概率发生，较大概率，随机和各种娱乐设施互动。
     * 4. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
     * 5. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。
     */
    Entertainment(HumanAction.Eating, HumanAction.Interaction, HumanAction.Entertainment, HumanAction.StrollingAround, HumanAction.LostInThought),
    /**
     * 1. 吃东西：当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
     * 2. 睡觉：当休息值小于20%时会睡觉。
     * 3. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
     * 4. 娱乐：概率发生，随机和各种娱乐设施互动。
     * 5. 命令工作：有命令工作时，会执行相应的命令。
     * 6. 普通工作：有工作时，会自动工作。
     * 7. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
     * 8. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。
     */
    FreeActivities(HumanAction.Eating, HumanAction.Sleep, HumanAction.Interaction, HumanAction.Entertainment, HumanAction.CommandWork, HumanAction.OrdinaryWork, HumanAction.StrollingAround, HumanAction.LostInThought),
    ;

    private final HumanAction[] actions;

    TimeAllocation(HumanAction... actions) {
        this.actions = actions;
    }

    public HumanAction[] getHumanAction() {
        return actions;
    }
}
