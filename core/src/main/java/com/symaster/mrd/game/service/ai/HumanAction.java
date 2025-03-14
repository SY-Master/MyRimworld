package com.symaster.mrd.game.service.ai;

import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.entity.Creature;
import com.symaster.mrd.game.entity.Database;
import com.symaster.mrd.game.entity.GameTime;
import com.symaster.mrd.game.entity.Measure;

import java.util.Random;

/**
 * @author yinmiao
 * @since 2025/1/3
 */
public enum HumanAction {

    /**
     * 1. 吃东西：当饱食度低于20%时会寻找食物，每次都会把饱食度吃满。
     */
    Eating(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            if (nodes.getFood() == null) {
                return false;
            }

            Measure food = nodes.getFood();
            return food.getValue() < 0.2f;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),
    /**
     * 2. 睡觉：当休息值小于20%时会睡觉。
     */
    Sleep(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            Measure energy = nodes.getEnergy();
            if (energy == null) {
                return false;
            }

            return energy.getValue() < 0.3f;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),
    /**
     * 3. 互动：概率发生，每个生物会主动根据策略寻找附近的生物互动，互动类型定义。
     */
    Interaction(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            if (RANDOM.nextFloat() < 0.2f) {


            }
            return false;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),
    Entertainment(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            return false;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),  //     4. 娱乐：概率发生，随机和各种娱乐设施互动。
    CommandWork(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            return false;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),  //     5. 命令工作：有命令工作时，会执行相应的命令。
    OrdinaryWork(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            return false;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),  //     6. 普通工作：有工作时，会自动工作。
    StrollingAround(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            return false;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),  //     7. 闲逛：每次会闲逛一定的时间，时间范围5-20s。
    LostInThought(new Action() {
        @Override
        public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
            return false;
        }

        @Override
        public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {

        }
    }),  //     8. 发呆：啥事都没有发生时，生物会发呆，时间范围5-20s。

    ;

    public static final Random RANDOM = new Random();
    private final Action action;

    HumanAction(Action action) {
        this.action = action;
    }


    public boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
        return action.available(scene, database, gameTime, nodes, delta);
    }

    public void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta) {
        action.logic(scene, database, gameTime, nodes, delta);
    }

    private interface Action {

        boolean available(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta);

        void logic(Scene scene, Database database, GameTime gameTime, Creature nodes, float delta);

    }
}
