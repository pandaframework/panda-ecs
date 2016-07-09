package org.pandaframework.ecs.system

import org.pandaframework.ecs.Aspect

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSystem {
    private val aspect: Aspect by lazy { aspect() }
    protected abstract fun aspect(): Aspect
}
