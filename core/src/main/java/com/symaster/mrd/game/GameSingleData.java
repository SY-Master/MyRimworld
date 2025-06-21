package com.symaster.mrd.game;

import com.symaster.mrd.MrAssetManager;
import com.symaster.mrd.api.AssetManagerProxy;
import com.symaster.mrd.api.PositionConverter;
import com.symaster.mrd.api.RootCamZoom;
import com.symaster.mrd.api.SkinProxy;
import com.symaster.mrd.game.service.PromptService;
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
    /**
     * 屏幕坐标与世界坐标的转换器
     */
    public static PositionConverter positionConverter;
    /**
     * 主相机的Zoom代理
     */
    public static RootCamZoom rootCamZoom;
    /**
     * 皮肤代理
     */
    public static SkinProxy skinProxy;
    /**
     * 资产管理器代理
     */
    // public static AssetManagerProxy assetManagerProxy;

    public static PromptService promptService;

    public static MrAssetManager mrAssetManager;

}
