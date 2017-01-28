package org.pandaframework.ecs.entity

import org.pandaframework.ecs.aspect.AspectBuilder
import org.pandaframework.ecs.aspect.AspectManager
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.pool.EntityPool
import kotlin.reflect.KClass

typealias Entity = Int

/**
 * @author Ranie Jade Ramiso
 */
class EntityManager internal constructor(
    private val aspectManager: AspectManager,
    private val entityPool: EntityPool
) {
    fun create(): Entity {
        TODO()
    }

    fun destroy(entity: Entity) {
        TODO()
    }

    fun <T: Component> mapper(component: KClass<T>): Mapper<T> {
        TODO()
    }

    fun blueprint(block: BlueprintBuilder.() -> Unit): Blueprint {
        TODO()
    }

    internal fun subscribe(block: AspectBuilder.() -> Unit): EntitySubscription {
        return with(aspectManager.builder()) {
            block(this)
            entityPool.subscribe(build())
        }
    }
}
