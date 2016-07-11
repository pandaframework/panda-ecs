package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
interface EntitySubscriptionManager: EntityManager {
    fun subscribe(aspect: Aspect): EntitySubscription
    fun edit(entity: Int): EntityEditor

}
