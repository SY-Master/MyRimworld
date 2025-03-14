package com.symaster.mrd.game;

import com.symaster.mrd.game.entity.Race;
import com.symaster.mrd.game.service.NameGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinmiao
 * @since 2024/12/30
 */
public class NameGeneratorFactory {

    private static final Map<Race, NameGenerator> nameGenerators = new HashMap<>();

    private static NameGenerator newGenerator(Race race)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<? extends NameGenerator> nameGenerator1 = race.getNameGenerator();

        Constructor<? extends NameGenerator> constructor = nameGenerator1.getConstructor();
        NameGenerator nameGenerator2 = constructor.newInstance();
        nameGenerators.put(race, nameGenerator2);
        return nameGenerator2;
    }

    public static NameGenerator getNameGenerator(Race race) {
        NameGenerator nameGenerator = nameGenerators.get(race);
        if (nameGenerator == null) {

            try {
                return newGenerator(race);
            } catch (NoSuchMethodException |
                     InstantiationException |
                     IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }


        return nameGenerator;
    }

}
