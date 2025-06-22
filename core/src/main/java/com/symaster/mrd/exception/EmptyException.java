package com.symaster.mrd.exception;

/**
 * @author yinmiao
 * @since 2025/6/22
 */
public class EmptyException extends RuntimeException {

    public EmptyException(String fit) {
        super(String.format("%s Is Empty.", fit));
    }

}
