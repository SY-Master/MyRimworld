package com.symaster.mrd.game;

import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.g2d.tansform.TransformInput;
import com.symaster.mrd.g2d.tansform.TransformMove;
import com.symaster.mrd.game.entity.Gender;
import com.symaster.mrd.game.entity.Human;
import com.symaster.mrd.game.entity.Measure;
import com.symaster.mrd.util.UnitUtil;

import java.util.Random;

/**
 * @author yinmiao
 * @since 2025/3/22
 */
public class NpcFactory {

    private static final Random random = new Random();

    public static Human randomHuman(float worldX, float worldY) {

        Human human = new Human();
        human.setZIndex(100);
        human.setActivityBlockSize(SystemConfig.PARTNER_ACTIVE_SIZE);
        human.setHp(new Measure(1, 100f));

        if (random.nextFloat() < 0.5) {
            human.setGender(Gender.FEMALE);
        } else {
            human.setGender(Gender.MALE);
        }

        TransformInput nodes = new TransformInput();
        human.add(nodes);

        TransformMove transformMove1 = new TransformMove(nodes, human);
        transformMove1.setSpeed(UnitUtil.ofM(5));

        human.add(transformMove1);
        human.setName(NameGeneratorFactory.getNameGenerator(human.getRace()).generateName(human.getGender()));
        human.setPosition(worldX, worldY);
        human.created();

        return human;
    }

}
