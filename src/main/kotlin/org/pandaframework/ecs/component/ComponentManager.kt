package org.pandaframework.ecs.component

import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal class ComponentManager {
    fun <T: Component> getIdentity(component: KClass<T>): ComponentIdentity<T> {
        throw UnsupportedOperationException()
    }

    fun <T: Component> getAllocator(component: ComponentIdentity<T>): ComponentAllocator<T> {
        throw UnsupportedOperationException()
    }
}
