package org.pandaframework.ecs.component

import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class ComponentManager {
    fun <T: Component> getId(component: KClass<T>): ComponentId {
        TODO()
    }
}