package com.symaster.mrd.game.service;

import com.symaster.mrd.game.entity.Gender;
import com.symaster.mrd.util.GdxText;

import java.util.Random;

/**
 * 人类名称生成器
 *
 * @author yinmiao
 * @since 2025/1/13
 */
public class HumanNameGenerator implements NameGenerator {

    // 姓
    private static final String[] firstNames = {
            GdxText.val("李"), GdxText.val("王"), GdxText.val("张"), GdxText.val("刘"), GdxText.val("陈"),
            GdxText.val("杨"), GdxText.val("赵"), GdxText.val("黄"), GdxText.val("周"), GdxText.val("吴"),
            GdxText.val("姬"), GdxText.val("姜"), GdxText.val("赢"), GdxText.val("姞"), GdxText.val("隗"),
            GdxText.val("嬴"), GdxText.val("姚"), GdxText.val("伊"), GdxText.val("羲"), GdxText.val("祝"),
            GdxText.val("陶"), GdxText.val("燕"), GdxText.val("姒"), GdxText.val("妘"), GdxText.val("妫"),
    };

    // 女名
    private static final String[] femaleLastNames = {
            GdxText.val("梦琪"), GdxText.val("忆柳"), GdxText.val("之桃"), GdxText.val("慕青"), GdxText.val("问兰"), GdxText.val("语蓉"),
            GdxText.val("尔岚"), GdxText.val("元香"), GdxText.val("初夏"), GdxText.val("沛菡"), GdxText.val("傲珊"), GdxText.val("曼文"),
            GdxText.val("乐菱"), GdxText.val("痴珊"), GdxText.val("恨玉"), GdxText.val("惜文"), GdxText.val("香寒"), GdxText.val("新柔"),
            GdxText.val("海安"), GdxText.val("夜蓉"), GdxText.val("宁熙"), GdxText.val("卿羽"), GdxText.val("嫣然"), GdxText.val("月华"),
            GdxText.val("若雪"), GdxText.val("落雪"), GdxText.val("明纾"), GdxText.val("尘月"), GdxText.val("瞑云"), GdxText.val("川烟"),
            GdxText.val("秋流"),
    };

    // 男名
    private static final String[] maleLastNames = {
            GdxText.val("华"), GdxText.val("伟"), GdxText.val("强"), GdxText.val("磊"), GdxText.val("军"), GdxText.val("洋"),
            GdxText.val("子涵"), GdxText.val("浩然"), GdxText.val("晨曦"), GdxText.val("峻熙"), GdxText.val("逸飞"), GdxText.val("睿泽"),
            GdxText.val("明杰"), GdxText.val("君浩"), GdxText.val("宇轩"), GdxText.val("浩淼"), GdxText.val("文博"), GdxText.val("俊豪"),
            GdxText.val("志远"), GdxText.val("建辉"), GdxText.val("杰森"), GdxText.val("鑫磊"), GdxText.val("伟强"), GdxText.val("泽宇"),
            GdxText.val("云帆"), GdxText.val("晓阳"), GdxText.val("子淳"), GdxText.val("昊天"), GdxText.val("飞鸿"), GdxText.val("书恒"),
            GdxText.val("润泽"), GdxText.val("锦程"), GdxText.val("宏图"), GdxText.val("紫瑞"), GdxText.val("承言"), GdxText.val("博远"),
    };

    // 随机数生成器
    private static final Random random = new Random();

    /**
     * 生成一个随机的人名
     *
     * @param gender 性别
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
