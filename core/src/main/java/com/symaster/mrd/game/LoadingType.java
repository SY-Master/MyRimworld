package com.symaster.mrd.game;

import com.symaster.mrd.Main;

/**
 * @author yinmiao
 * @since 2025/1/2
 */
public enum LoadingType {

    ApplicationRunnerLoading(Main::applicationRunnerLoading),
    GamePlayLoading(Main::gamePlayLoading),
    ;

    private final RenderProxy renderProxy;

    LoadingType(RenderProxy renderProxy) {
        this.renderProxy = renderProxy;
    }

    public void render(Main main, float delta) {
        renderProxy.render(main, delta);
    }

    private interface RenderProxy {
        void render(Main main, float delta);
    }
}
