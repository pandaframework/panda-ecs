package org.pandaframework.ecs.state

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.Blueprint
import org.pandaframework.ecs.entity.BlueprintBuilder
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.Mapper

/**
 * @author Ranie Jade Ramiso
 */
abstract class StateHandler<T: State> {
    internal lateinit var _entityManager: EntityManager
    internal lateinit var _stateManager: StateManager<*>

    val entityManager: EntityManager by lazy { _entityManager }
    val stateManager: StateManager<T> by lazy { _stateManager as StateManager<T> }

    abstract fun setup()
    abstract fun cleanup()

    protected inline fun <reified T: Component> mapper(): Lazy<Mapper<T>> {
        return lazy {
            entityManager.mapper(T::class)
        }
    }

    protected fun blueprint(block: BlueprintBuilder.() -> Unit): Lazy<Blueprint> {
        return lazy { entityManager.blueprint(block) }
    }
}
