package org.pandaframework.ecs.util.pool

import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
class BasicObjectPool<T>(val keepAlive: Int, val factory: () -> T): ObjectPool<T> {
    private val limbo: MutableSet<T> = HashSet()
    private val live: MutableSet<T> = HashSet()

    override fun acquire(): T {
        val instance = if (limbo.isNotEmpty()) {
            limbo.first().apply {
                limbo.remove(this)
            }
        } else {
            factory()
        }

        live.add(instance)
        return instance
    }

    override fun release(instance: T) {
        live.remove(instance)
        limbo.add(instance)

        val drop = limbo.size - keepAlive

        if (drop > 0) {
            val iterator = limbo.iterator()
            (1..drop).forEach {
                iterator.next()
                iterator.remove()
            }
        }
    }
}
