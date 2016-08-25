package org.pandaframework.ecs.util.pool

/**
 * @author Ranie Jade Ramiso
 */
interface ObjectPool<T> {
    fun acquire(): T
    fun release(instance: T)
}
