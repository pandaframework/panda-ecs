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
class EntityManager constructor(private val aspectManager: AspectManager,
                                private val entityPool: EntityPool) {

    private val mapperInstances = mutableMapOf<KClass<out Component>, Mapper<*>>()

    fun create(): Entity {
        return entityPool.create()
    }

    fun destroy(entity: Entity) {
        entityPool.destroy(entity)
    }

    @Suppress("unchecked_cast")
    fun <T: Component> mapper(component: KClass<T>): Mapper<T> {
        return mapperInstances.getOrPut(component) {
            object: Mapper<T> {
                override fun get(entity: Entity): T {
                    return entityPool.edit(entity)
                        .get(component)
                }

                override fun contains(entity: Entity): Boolean {
                    return entityPool.edit(entity)
                        .contains(component)
                }

                override fun remove(entity: Entity) {
                    entityPool.edit(entity)
                        .remove(component)
                }

            }
        } as Mapper<T>
    }

    fun blueprint(block: BlueprintBuilder.() -> Unit): Blueprint {
        return with(BlueprintBuilder(entityPool)) {
            block(this)
            build()
        }
    }

    internal fun subscribe(block: AspectBuilder.() -> Unit): EntitySubscription {
        return with(aspectManager.builder()) {
            block(this)
            entityPool.subscribe(build())
        }
    }
}
