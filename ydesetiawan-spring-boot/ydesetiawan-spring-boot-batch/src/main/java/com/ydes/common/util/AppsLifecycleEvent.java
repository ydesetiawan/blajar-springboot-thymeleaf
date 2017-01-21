package com.ydes.common.util;

import org.springframework.context.ApplicationEvent;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
public class AppsLifecycleEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public AppsLifecycleEvent(Object source) {
        super(source);
    }
}
