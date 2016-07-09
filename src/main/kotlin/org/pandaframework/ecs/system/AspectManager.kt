package org.pandaframework.ecs.system

import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface AspectManager {
    fun aspectFor(system: KClass<out AbstractSystem>): Aspect
}
