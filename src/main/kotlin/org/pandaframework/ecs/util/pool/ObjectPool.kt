package org.pandaframework.ecs.util.pool

/**
 * @author Ranie Jade Ramiso
 */
interface ObjectPool<T: Any> {
    fun acquire(): T
    fun release(instance: T)
}
