package org.pandaframework.ecs.state

/**
 * @author Ranie Jade Ramiso
 */
class StateManager<T: State>(internal val states: Map<T, StateHandler<*>>) {

    private var currentState: T? = null

    fun setState(state: T) {
        if (currentState != null) {
            if (state == currentState) {
                throw IllegalArgumentException("Already in $state.")
            }

            cleanupState(state)
        }

        currentState = state
        setupState(state)
    }

    fun currentState(): T = currentState!!


    private fun setupState(state: T) {
        states.getValue(state).setup()
    }

    private fun cleanupState(state: T) {
        states.getValue(state).cleanup()
    }
}
