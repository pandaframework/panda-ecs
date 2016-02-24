package org.pandaframework.ecs.component

/**
 * Allocate and de-allocate instances of a [T].
 *
 * @author Ranie Jade Ramiso
 */
internal interface ComponentAllocator<T: Component> {
    fun allocate(): T
    fun deallocate(component: T)
}
