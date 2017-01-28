package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.pool.EntityPool
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal class MapperImpl<out T: Component>(private val entityPool: EntityPool,
                                   private val component: KClass<T>): Mapper<T> {

    override fun get(entity: Entity): T {
        return entityPool.edit(entity)
            .get(component)
    }

    override fun contains(entity: Entity) = entityPool.edit(entity)
        .contains(component)

    override fun remove(entity: Entity) {
        entityPool.edit(entity)
            .remove(component)
    }
}
