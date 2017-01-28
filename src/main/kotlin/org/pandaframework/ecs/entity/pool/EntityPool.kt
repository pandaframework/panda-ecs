package org.pandaframework.ecs.entity.pool

import org.pandaframework.ecs.aspect.Aspect
import org.pandaframework.ecs.entity.Entity
import org.pandaframework.ecs.entity.EntitySubscription

/**
 * @author Ranie Jade Ramiso
 */
internal interface EntityPool {
    fun create(): Entity
    fun edit(entity: Entity): EntityEditor
    fun destroy(entity: Entity)
    fun subscribe(aspect: Aspect): EntitySubscription
}
