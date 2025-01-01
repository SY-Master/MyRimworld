package com.symaster.mrd.game.service;

import com.symaster.mrd.game.entity.Gender;

/**
 * 名称生成器
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public interface NameGenerator {

    String generateName(Gender gender);

}
