package org.pandaframework.ecs.system

import org.pandaframework.ecs.entity.Entity

/**
 * @author Ranie Jade Ramiso
 */
interface IteratingSystem {
    fun update(time: Double, entity: Entity)
}


class IteratingUpdateStrategy<out T>(val system: T): UpdateStrategy
    where T: IteratingSystem, T: System<*> {

    override fun update(time: Double) {
        system.subscription().entities()
            .forEach { system.update(time, it) }
    }
}
