package com.symaster.mrd.game.service;

import java.util.Random;

public class HumanNameGenerator implements NameGenerator {

    // 常见的名字列表
    private static final String[] firstNames = {
            "李", "王", "张", "刘", "陈",
            "杨", "赵", "黄", "周", "吴"
            // 可以继续添加更多的名字...
    };

    // 常见的姓氏列表
    private static final String[] lastNames = {
            "华", "伟", "芳", "敏", "静",
            "丽", "强", "磊", "军", "洋"
            // 可以继续添加更多的姓氏...
    };

    // 随机数生成器
    private static final Random random = new Random();

    /**
     * 生成一个随机的人名
     *
     * @return 生成的随机人名
     */
    @Override
    public String generateName() {
        // 随机选择一个名字和一个姓氏
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        // 组合并返回人名
        return firstName + lastName;
    }
}
