package org.pandaframework.ecs.entity

import org.pandaframework.ecs.MapperDelegate
import org.pandaframework.ecs.component.Component
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface EntityManager {
    fun create(): Int
    fun remove(entity: Int)
    fun <T: Component> mapper(component: KClass<T>): MapperDelegate<T>
}
