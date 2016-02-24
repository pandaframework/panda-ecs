package org.pandaframework.ecs.component

/**
 * @author Ranie Jade Ramiso
 */
interface ComponentMapper<out T: Component> {
    fun get(entityId: Int): T
}
