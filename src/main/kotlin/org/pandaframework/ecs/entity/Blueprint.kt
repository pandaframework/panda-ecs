package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface Blueprint {
    fun create(): Entity
    fun transmute(entity: Entity)
}

class BlueprintBuilder(val entityManager: EntityManager) {
    private val components = mutableListOf<KClass<out Component>>()

    inline fun <reified T: Component> component() {
        component(T::class)
    }

    @PublishedApi
    internal fun <T: Component> component(component: KClass<T>) {
        components.add(component)
    }

    internal fun build(): Blueprint {
        return object: Blueprint {
            override fun transmute(entity: Entity) {
                TODO()
            }

            override fun create(): Entity {
                TODO()
            }
        }
    }
}
