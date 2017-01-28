package org.pandaframework.ecs

import org.pandaframework.ecs.aspect.AspectManager
import org.pandaframework.ecs.component.ComponentManager
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.pool.BasicEntityPool
import org.pandaframework.ecs.state.State
import org.pandaframework.ecs.state.StateHandler
import org.pandaframework.ecs.state.StateManager
import org.pandaframework.ecs.system.System


inline fun <reified T: State> createWorld(block: WorldBuilder<T>.() -> Unit): World<T> {
    return with(WorldBuilder<T>()) {
        block(this)
        build()
    }
}

/**
 * @author Ranie Jade Ramiso
 */
class WorldBuilder<T: State> internal @PublishedApi constructor() {
    private val systems = mutableListOf<System<T>>()
    private val stateHandlers = HashMap<T, StateHandler<*>>()

    fun <S: System<T>> registerSystem(system: S) {
        systems.add(system)
    }

    fun <S: T> registerStateHandler(state: S, handler: StateHandler<S>) {
        stateHandlers.put(state, handler)
    }

    @PublishedApi
    internal fun build(): World<T> {

        val componentManager = ComponentManager()
        val aspectManager = AspectManager(componentManager)
        val entityPool = BasicEntityPool(componentManager)

        val entityManager = EntityManager(
            aspectManager, entityPool
        )
        val stateManager = StateManager(stateHandlers)

        return World(entityManager, stateManager, systems)
    }
}
