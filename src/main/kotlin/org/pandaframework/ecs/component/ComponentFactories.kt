package org.pandaframework.ecs.component

import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface ComponentFactories {
    fun <T: Component> factoryFor(component: KClass<T>): ComponentFactory<T>
}
