package com.symaster.mrd.game.service;

import com.symaster.mrd.game.entity.Creature;

/**
 * @author yinmiao
 * @since 2025/5/4
 */
public class DssProxy {

    private final DSS dss;

    private final Creature creature;

    public DssProxy(DSS dss, Creature creature) {
        this.dss = dss;
        this.creature = creature;
    }

    public DSS getDss() {
        return dss;
    }

    public Creature getCreature() {
        return creature;
    }

}
