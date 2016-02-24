package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import kotlin.reflect.KClass

fun aspect(init: Aspect.() -> Unit): Aspect {
    val aspect = Aspect()
    aspect.init()
    return aspect
}

/**
 * @author Ranie Jade Ramiso
 */
class Aspect internal constructor() {
    internal val allFilter: MutableSet<KClass<in Component>> = mutableSetOf()
    internal val anyFilter: MutableSet<KClass<in Component>> = mutableSetOf()
    internal val excludeFilter: MutableSet<KClass<in Component>> = mutableSetOf()

    fun all(components: List<KClass<Component>>) {
        allFilter.addAll(components)
    }

    fun all(vararg components: KClass<Component>) {
        anyFilter.addAll(listOf(*components))
    }

    fun any(components: List<KClass<Component>>) {
        anyFilter.addAll(components)
    }

    fun any(vararg components: KClass<Component>) {
        allFilter.addAll(listOf(*components))
    }

    fun exclude(components: List<KClass<Component>>) {
        excludeFilter.addAll(components)
    }

    fun exclude(vararg components: KClass<Component>) {
        excludeFilter.addAll(listOf(*components))
    }

}
