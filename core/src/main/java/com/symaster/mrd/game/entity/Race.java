package com.symaster.mrd.game.entity;

import com.symaster.mrd.game.service.HumanNameGenerator;
import com.symaster.mrd.game.service.NameGenerator;

/**
 * 种族
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public enum Race {

    Human("人类", com.symaster.mrd.game.entity.Human.class, HumanNameGenerator.class),

    ;

    private final String name;
    private final Class<? extends Creature> race;
    private final Class<? extends NameGenerator> nameGenerator;

    Race(String name, Class<? extends Creature> race, Class<? extends NameGenerator> nameGenerator) {
        this.name = name;
        this.race = race;
        this.nameGenerator = nameGenerator;
    }

    public String getName() {
        return name;
    }

    public Class<?> getRace() {
        return race;
    }

    public Class<? extends NameGenerator> getNameGenerator() {
        return nameGenerator;
    }
}
