package com.ydes.common.util;

import org.springframework.context.ApplicationEvent;

/**
 * @author edys
 * @version 1.0, Jul 7, 2014
 * @since 3.1.1
 */
public class AppsLifecycleEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public AppsLifecycleEvent(Object source) {
        super(source);
    }
}
