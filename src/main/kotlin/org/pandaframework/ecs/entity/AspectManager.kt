package org.pandaframework.ecs.entity

import org.pandaframework.ecs.system.AbstractSystem
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface AspectManager {
    fun aspectFor(system: KClass<out AbstractSystem>): AspectImpl
}
