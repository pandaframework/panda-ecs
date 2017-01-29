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
                      val initialState: T,
                      val systems: List<System<T>>) {

    fun setup() {
        stateManager.setup()
        stateManager.transitionTo(initialState)

        systems.forEach {
            it._entityManager = entityManager
            it._stateManager = stateManager

            it.bootstrap()
            it.setup()
        }
    }

    fun update(time: Double) {
        stateManager.process()

        systems
            .filter {
                it.supportedStates.isEmpty() or it.supportedStates.contains(stateManager.currentState())
            }
            .forEach { it.update(time) }
    }

    fun cleanup() {
        systems.forEach(System<T>::cleanup)
        stateManager.cleanup()
    }
}
