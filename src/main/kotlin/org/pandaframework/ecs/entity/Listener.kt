package org.pandaframework.ecs.entity

interface EntitySubscriptionListener {
    fun inserted(entity: Entity)
    fun removed(entity: Entity)
}
