package org.pandaframework.ecs.entity

interface EntitySubscriptionListener {
    fun added(entity: Entity)
    fun removed(entity: Entity)
}
