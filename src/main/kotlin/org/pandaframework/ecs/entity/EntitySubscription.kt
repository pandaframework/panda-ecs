package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
interface EntitySubscription {
    fun addListener(listener: EntitySubscriptionListener)
    fun removeListener(listener: EntitySubscriptionListener)

    fun entities(): IntArray

}
