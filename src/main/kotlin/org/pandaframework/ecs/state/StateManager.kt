package org.pandaframework.ecs.state

import org.pandaframework.ecs.entity.EntityManager

/**
 * @author Ranie Jade Ramiso
 */
class StateManager<T: State>(internal val entityManager: EntityManager,
                             internal val handlers: Map<T, StateHandler<*>>) {

    private var currentState: T? = null
    private var nextState: T? = null

    internal fun setup() {
        handlers.forEach {
            it.value._entityManager = entityManager
            it.value._stateManager = this
        }
    }

    internal fun process() {
        if (nextState != null) {
            if (currentState != null) {
                cleanupState(currentState!!)
            }

            currentState = nextState
            nextState = null
            setupState(currentState!!)
        }
    }

    internal fun cleanup() {
        cleanupState(currentState())
    }

    fun transitionTo(state: T) {
        require(currentState != state, { "Already in state: $state" })
        nextState = state
    }

    fun currentState(): T = currentState!!


    private fun setupState(state: T) {
        handlers.getValue(state).setup()
    }

    private fun cleanupState(state: T) {
        handlers.getValue(state).cleanup()
    }
}
