package org.pandaframework.ecs.entity.pool

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.Entity
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface EntityEditor {
    val entity: Entity

    fun <T: Component> get(component: KClass<T>): T
    fun <T: Component> contains(component: KClass<T>): Boolean
    fun <T: Component> remove(component: KClass<T>)
}
