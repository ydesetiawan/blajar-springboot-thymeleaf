package com.ydes.common.util;

/**
 * @author edys
 * @version 1.0, Jul 7, 2014
 * @since 3.1.1
 */
public class AppsLifecycleShutdownEvent extends AppsLifecycleEvent {

    private static final long serialVersionUID = 1L;

    public AppsLifecycleShutdownEvent(Object source) {
        super(source);
    }
}
