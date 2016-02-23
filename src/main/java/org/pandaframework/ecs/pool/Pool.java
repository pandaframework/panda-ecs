package org.pandaframework.ecs.pool;

/**
 * @author Ranie Jade Ramiso
 */
public interface Pool<T extends Poolable> {
    T claim();
    void release(T object);
}
