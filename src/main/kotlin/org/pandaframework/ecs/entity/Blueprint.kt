package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.pool.EntityPool
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface Blueprint {
    fun create(): Entity
    // TODO: fun transmute(entity: Entity)
}

class BlueprintBuilder(val entityPool: EntityPool) {
    private val components = mutableListOf<KClass<out Component>>()

    inline fun <reified T: Component> withComponent() {
        withComponent(T::class)
    }

    @PublishedApi
    internal fun <T: Component> withComponent(component: KClass<T>) {
        components.add(component)
    }

    internal fun build(): Blueprint {
        return object: Blueprint {
            override fun create(): Entity {
                val entity = entityPool.create()
                entityPool.edit(entity).apply {
                    components.forEach { get(it) }
                }
                return entity
            }
        }
    }
}
