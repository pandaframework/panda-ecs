package org.pandaframework.ecs.entity

import org.pandaframework.ecs.system.AbstractSystem

/**
 * @author Ranie Jade Ramiso
 */
internal interface AspectManager {
    fun aspectFor(system: AbstractSystem): AspectImpl
}
