package com.symaster.mrd.g2d.scene;

import com.symaster.mrd.g2d.Node;

import java.io.Serializable;

public final class MoveNodeCache implements Serializable {

    private static final long serialVersionUID = 1L;
    public Node node;
    public float oldX, oldY, newX, newY;
}
