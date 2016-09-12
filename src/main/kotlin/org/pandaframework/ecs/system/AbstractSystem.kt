package org.pandaframework.ecs.system

import org.pandaframework.ecs.MapperDelegate
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.EntitySubscription

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSystem(protected val entityManager: EntityManager,
                              protected val subscription: EntitySubscription) {
    abstract fun aspect(aspect: Aspect)

    open fun init() { }

    abstract fun update(delta: Float)

    open fun destroy() { }

    protected inline fun <reified T: Component> mapper(): MapperDelegate<T> = entityManager.mapper(T::class)
}
