package org.pandaframework.ecs.system

import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.EntitySubscription

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractIteratingSystem(entityManager: EntityManager,
                                       subscription: EntitySubscription): AbstractSystem(entityManager, subscription) {
    override fun update(delta: Float) {
        subscription.entities().forEach {
            entity(delta, it)
        }
    }

    protected abstract fun entity(delta: Float, entity: Int)
}
