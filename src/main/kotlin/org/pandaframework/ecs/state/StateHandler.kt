package org.pandaframework.ecs.state

import org.pandaframework.ecs.entity.EntityManager

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
}
