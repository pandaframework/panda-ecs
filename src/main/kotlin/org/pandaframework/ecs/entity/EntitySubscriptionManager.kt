package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
internal class EntitySubscriptionManager {
    class EntitySubscriptionImpl : EntitySubscription {
        override fun entities(): Set<Int> = emptySet()
        override fun subscribe(listener: EntitySubscription.Listener) {}
        override fun unsubscribe(listener: EntitySubscription.Listener) {}
    }

    fun entities(aspect: Aspect): EntitySubscription {
        return EntitySubscriptionImpl()
    }
}
