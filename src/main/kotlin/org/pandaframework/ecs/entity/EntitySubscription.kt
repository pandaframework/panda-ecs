package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
interface EntitySubscription {

    interface Listener {
        fun inserted(entities: Set<Int>)
        fun removed(entities: Set<Int>)
    }

    fun subscribe(listener: Listener)
    fun unsubscribe(listener: Listener)
    fun entities(): Set<Int>
}
