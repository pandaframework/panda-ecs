package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
internal interface EntitySubscriptionManager: EntityManager {
    fun subscribe(aspect: Aspect): EntitySubscription
    fun edit(entity: Int): EntityEditor

}
