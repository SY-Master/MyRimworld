package com.symaster.mrd.game;

import com.symaster.mrd.api.PositionConverter;
import com.symaster.mrd.input.InputBridge;

/**
 * 游戏数据库
 *
 * @author yinmiao
 * @since 2025/1/2
 */
public class GameSingleData {

    /**
     * 输入桥接器
     */
    public static InputBridge inputBridge;
    /**
     * 游戏界面状态
     */
    public static GamePageStatus gamePageStatus;
    /**
     * 加载类型
     */
    public static LoadingType loadingType;

    public static PositionConverter positionConverter;

}
