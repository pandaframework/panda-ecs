package org.pandaframework.ecs

import org.pandaframework.ecs.component.Component

/**
 * @author Ranie Jade Ramiso
 */
interface Mapper<out T: Component> {
    fun get(entity: Int): T
    fun contains(entity: Int)
    fun remove(entity: Int)
}
