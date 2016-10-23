package org.pandaframework.ecs.system

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractIteratingSystem(): AbstractSystem() {
    override fun update(delta: Float) {
        subscription.entities().forEach {
            entity(delta, it)
        }
    }

    protected abstract fun entity(delta: Float, entity: Int)
}
