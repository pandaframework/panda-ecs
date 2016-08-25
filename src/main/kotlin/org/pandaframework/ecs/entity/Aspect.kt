package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.util.Bits
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface Aspect {
    fun all(vararg components: KClass<out Component>): Aspect
    fun any(vararg components: KClass<out Component>): Aspect
    fun exclude(vararg components: KClass<out Component>): Aspect
}
