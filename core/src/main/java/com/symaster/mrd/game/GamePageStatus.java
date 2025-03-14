package com.symaster.mrd.game;


import com.symaster.mrd.Main;

/**
 * @author yinmiao
 * @since 2025/1/2
 */
public enum GamePageStatus {

    Loading((main, delta) -> {
        GameSingleData.loadingType.render(main, delta);
    }),
    Menu(Main::menuRender),
    Game(Main::gameRender),
    ;

    private final RenderProxy renderProxy;

    GamePageStatus(RenderProxy renderProxy) {
        this.renderProxy = renderProxy;
    }

    public void render(Main main, float delta) {
        renderProxy.render(main, delta);
    }

    private interface RenderProxy {

        void render(Main main, float delta);

    }
}
