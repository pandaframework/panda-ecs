package org.pandaframework.ecs.system

import org.pandaframework.ecs.system.Aspect

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSystem {
    abstract fun aspect(aspect: Aspect)

    open fun initialize() { }

    abstract fun update(delta: Float, entities: IntArray)

    open fun destroy() { }
}
