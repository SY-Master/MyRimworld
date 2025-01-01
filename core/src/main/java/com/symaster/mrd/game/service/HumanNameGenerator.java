package com.symaster.mrd.game.service;

import com.symaster.mrd.game.entity.Gender;

import java.util.Random;

public class HumanNameGenerator implements NameGenerator {

    // 姓
    private static final String[] firstNames = {
            "李", "王", "张", "刘", "陈",
            "杨", "赵", "黄", "周", "吴",
            "姬", "姜", "赢", "姞", "隗",
            "嬴", "姚", "伊", "羲", "祝",
            "陶", "燕", "姒", "妘", "妫",
    };

    // 女名
    private static final String[] femaleLastNames = {
            "梦琪", "忆柳", "之桃", "慕青", "问兰", "语蓉",
            "尔岚", "元香", "初夏", "沛菡", "傲珊", "曼文",
            "乐菱", "痴珊", "恨玉", "惜文", "香寒", "新柔",
            "海安", "夜蓉", "宁熙", "卿羽", "嫣然", "月华",
            "若雪", "落雪", "明纾", "尘月", "瞑云", "川烟",
            "秋流",
    };

    // 男名
    private static final String[] maleLastNames = {
            "华", "伟", "强", "磊", "军", "洋",
            "子涵", "浩然", "晨曦", "峻熙", "逸飞", "睿泽",
            "明杰", "君浩", "宇轩", "浩淼", "文博", "俊豪",
            "志远", "建辉", "杰森", "鑫磊", "伟强", "泽宇",
            "云帆", "晓阳", "子淳", "昊天", "飞鸿", "书恒",
            "润泽", "锦程", "宏图", "紫瑞", "承言", "博远",
    };

    // 随机数生成器
    private static final Random random = new Random();

    /**
     * 生成一个随机的人名
     *
     * @return 生成的随机人名
     */
    @Override
    public String generateName(Gender gender) {
        // 随机选择一个名字和一个姓氏
        String firstName = firstNames[random.nextInt(firstNames.length)];

        String lastName;
        if (gender == Gender.FEMALE) {
            lastName = femaleLastNames[random.nextInt(femaleLastNames.length)];
        } else {
            lastName = maleLastNames[random.nextInt(maleLastNames.length)];
        }


        // 组合并返回人名
        return firstName + lastName;
    }
}
