package org.pandaframework.ecs

import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.state.State
import org.pandaframework.ecs.state.StateManager
import org.pandaframework.ecs.system.System

/**
 * @author Ranie Jade Ramiso
 */
class World<T: State>(val entityManager: EntityManager,
                      val stateManager: StateManager<T>,
                      private val systems: List<System<T>>) {

    fun setup() {
        systems.forEach {
            it._entityManager = entityManager
            it._stateManager = stateManager

            stateManager.states.values.forEach {
                it._entityManager = entityManager
                it._stateManager = stateManager
            }

            it.bootstrap()
            it.setup()
        }
    }

    fun update(time: Double) {
        systems.forEach { it.update(time) }
    }

    fun cleanup() {
        systems.forEach(System<T>::cleanup)
    }
}
