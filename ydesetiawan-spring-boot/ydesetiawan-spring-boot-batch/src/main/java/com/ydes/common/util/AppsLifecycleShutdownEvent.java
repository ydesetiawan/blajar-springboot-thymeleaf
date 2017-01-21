package com.ydes.common.util;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
public class AppsLifecycleShutdownEvent extends AppsLifecycleEvent {

    private static final long serialVersionUID = 1L;

    public AppsLifecycleShutdownEvent(Object source) {
        super(source);
    }
}
