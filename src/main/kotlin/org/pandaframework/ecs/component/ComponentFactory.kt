package org.pandaframework.ecs.component

/**
 * @author Ranie Jade Ramiso
 */
interface ComponentFactory<T: Component> {
    fun create(): T
    fun release(instance: T)
}
