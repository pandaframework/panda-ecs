package org.pandaframework.ecs.system

import org.pandaframework.ecs.World
import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.entity.EntitySubscription

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSystem protected constructor(val aspect: Aspect): EntitySubscription.Listener {
    var enabled: Boolean = true
        protected set

    private lateinit var subscription: EntitySubscription
    internal lateinit var world: World

    open fun initialize() {
        subscription = world.entitySubscriptionManager.entities(aspect)
    }

    internal open fun process(delta: Float) {
        subscription.entities().forEach {
            process(it, delta)
        }
    }

    open fun destroy() {
    }

    open fun canProcess(delta: Float): Boolean = true

    override fun inserted(entities: Set<Int>) {}

    override fun removed(entities: Set<Int>) {}

    protected abstract fun process(entity: Int, delta: Float)
}
