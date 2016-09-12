package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
interface EntityManager {
    fun create(): Int
    fun remove(entity: Int)
    fun edit(entity: Int): EntityEditor
}
