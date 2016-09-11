package org.pandaframework.ecs.system

import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.EntitySubscription

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSystem {
    internal lateinit var _entityManager: EntityManager
    internal lateinit var _subscription: EntitySubscription

    protected val entityManager: EntityManager
        get() = _entityManager

    protected val subscription: EntitySubscription
        get() = _subscription

    abstract fun aspect(aspect: Aspect)

    open fun init() { }

    abstract fun update(delta: Float)

    open fun destroy() { }
}
