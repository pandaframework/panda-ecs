package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal interface EntityEditor {
    /**
     * Retrieve or create an instance of [T] for the entity operated by this editor.
     */
    fun <T: Component> getComponent(component: KClass<T>): T

    /**
     * Remove the specific component, from the entity operated by this editor.
     */
    fun removeComponent(component: KClass<out Component>)

    /**
     * Check whether a component is present or not.
     */
    fun hasComponent(component: KClass<out Component>): Boolean
}
