package org.pandaframework.ecs

import org.pandaframework.ecs.component.Component

/**
 * @author Ranie Jade Ramiso
 */
interface Mapper<out T: Component> {
    operator fun get(entity: Int): T
    fun contains(entity: Int): Boolean
    fun remove(entity: Int)
}
