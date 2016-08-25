package org.pandaframework.ecs.entity

/**
 * @author Ranie Jade Ramiso
 */
class EntityNotFoundException(val entity: Int): Throwable(
    "Entity '$entity' not found"
)
