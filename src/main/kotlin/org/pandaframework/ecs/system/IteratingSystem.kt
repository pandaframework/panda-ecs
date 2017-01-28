package org.pandaframework.ecs.system

import org.pandaframework.ecs.entity.Entity
import org.pandaframework.ecs.state.State

/**
 * @author Ranie Jade Ramiso
 */
abstract class IteratingSystem<T: State>: System<T>() {
    override final fun update(time: Double) {
        subscription().entities().forEach {
            update(time, it)
        }
    }

    protected abstract fun update(time: Double, entity: Entity)
}
