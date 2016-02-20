package org.pandaframework.ecs.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Ranie Jade Ramiso
 */
public interface Component {
    void release();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface PoolConfig {
        int maxSize();
        long idleTimeout();
        TimeUnit unit();
    }
}
