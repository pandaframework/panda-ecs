package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface EntityEditor {
    fun <T: Component> addComponent(component: KClass<T>): T
    fun removeComponent(component: KClass<out Component>)
    fun hasComponent(component: KClass<out Component>): Boolean
}
