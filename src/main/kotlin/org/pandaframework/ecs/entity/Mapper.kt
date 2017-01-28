package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component

/**
 * @author Ranie Jade Ramiso
 */
interface Mapper<out T: Component> {
    fun get(entity: Entity): T
    fun contains(entity: Entity): Boolean
    fun remove(entity: Entity)
}
