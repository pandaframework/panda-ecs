package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
interface EntitySubscription {
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
    fun entities(): IntArray

    interface Listener {
        fun entityAdded(entity: Int)
        fun entityRemoved(entity: Int)
    }
}
