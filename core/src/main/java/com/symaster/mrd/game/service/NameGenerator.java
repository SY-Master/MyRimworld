package com.symaster.mrd.game.service;

import com.symaster.mrd.game.entity.Gender;

/**
 * 名称生成器
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public interface NameGenerator {

    /**
     * 生成名字
     *
     * @param gender 性别
     * @return 生成后的名字
     */
    String generateName(Gender gender);

}
