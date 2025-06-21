package com.symaster.mrd.util;

import com.symaster.modify.Modify;
import com.symaster.modify.ModifyOption;
import com.symaster.mrd.game.service.DssProxy;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2025/4/12
 */
public enum ModifyOptionsEnum {

    MOVE(Pattern.compile("\\{\\{move::([0-9.\\-]*)::([0-9.\\-]*)}}"),
         "{{move::x::y}}：移动指令，x和y组成一个移动向量，类型为double", (matcher, input, ins) -> {
        String g1 = matcher.group(1).trim();
        String g2 = matcher.group(2).trim();

        return matcher.replaceFirst("");
    }),

    SAY(Pattern.compile("\\{\\{say::([^:]*)::(.*)}}"),
        "{{say::dstName::content}}：说话指令。dstName是和谁说话，这里可以设置特殊关键字“all”代表和附近的所有人说话。content为需要说的内容。",
        (matcher, input, ins) -> {
            String g1 = matcher.group(1);
            String g2 = matcher.group(2);

            return matcher.replaceFirst("");
        }),

    ATTACK(Pattern.compile("\\{\\{attack::([^:]*)::(.*)}}"),
           "{{attack::dstName::attackType}}：攻击指令。dstName是需要攻击谁的名称。attackType是攻击方式，详情请参见“ATTACK_TYPE”项。",
           (matcher, input, ins) -> {
               String g1 = matcher.group(1);
               String g2 = matcher.group(2);

               return matcher.replaceFirst("");
           }),

    SKIP(Pattern.compile("\\{\\{skip}}"), "{{skip}}：什么都不做。", (matcher, input, ins) -> {

        return matcher.replaceFirst("");
    }),

    LOOK(Pattern.compile("\\{\\{look::([^:]*)::([^:{}]*)}}"),
         "{{look::dstName::part}}：看向某个人。dstName是需要看向谁的名称。part是指看向谁的具体身体的哪个部位",
         (matcher, input, ins) -> {
             String g1 = matcher.group(1);
             String g2 = matcher.group(2);

             return matcher.replaceFirst("");
         }),

    STOP(Pattern.compile("\\{\\{stop}}"), "{{stop}}：停止当前会话的指令，停止当前正在进行的会话。",
         (matcher, input, ins) -> {

             return matcher.replaceFirst("");
         }),

    FOLLOW(Pattern.compile("\\{\\{follow::([^:]*)::([0-9.\\-]*)}}"),
           "{{follow::dstName::distance}}：跟随某人的指令，dstName是跟随谁，distance是跟随距离（米）。",
           (matcher, input, ins) -> {
               String g1 = matcher.group(1);
               String g2 = matcher.group(2);

               return matcher.replaceFirst("");
           }),

    CANCEL_FOLLOW(Pattern.compile("\\{\\{cancelFollow}}"), "{{cancelFollow}}：取消跟随指令，取消当前正在跟随的人。",
                  (matcher, input, ins) -> {

                      return matcher.replaceFirst("");
                  }),

    FEELING(Pattern.compile("\\{\\{feeling::([^:]*)::(.*)}}"),
            "{{feeling::dstName::content}}：更新你对谁的看法，dstName是需要更新谁，content是看法",
            (matcher, input, ins) -> {
                String g1 = matcher.group(1);
                String g2 = matcher.group(2);

                return matcher.replaceFirst("");
            }),

    ;

    private final Pattern compile;
    private final String disc;
    private final Modify<DssProxy> modify;

    ModifyOptionsEnum(Pattern compile, String disc, Modify<DssProxy> modify) {
        this.compile = compile;
        this.disc = disc;
        this.modify = modify;
    }

    public static List<ModifyOption<DssProxy>> modifyList() {
        return Arrays.stream(ModifyOptionsEnum.values()).map(e -> {
            ModifyOption<DssProxy> modifyResult = new ModifyOption<>();
            modifyResult.setPattern(e.getCompile());
            modifyResult.setModify(e.getModify());
            return modifyResult;
        }).collect(Collectors.toList());
    }

    public static String disc() {
        return Arrays.stream(ModifyOptionsEnum.values())
                     .map(ModifyOptionsEnum::getDisc)
                     .collect(Collectors.joining("\n"));
    }

    public Pattern getCompile() {
        return compile;
    }

    public Modify<DssProxy> getModify() {
        return modify;
    }

    public String getDisc() {
        return disc;
    }
}

