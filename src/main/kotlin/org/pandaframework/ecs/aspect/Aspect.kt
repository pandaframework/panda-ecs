package org.pandaframework.ecs.aspect

import org.pandaframework.ecs.component.ComponentBits

/**
 * @author Ranie Jade Ramiso
 */
internal data class Aspect(
    val required: ComponentBits,
    val optional: ComponentBits,
    val excluded: ComponentBits
) {
    fun match(componentBits: ComponentBits): Boolean {
        return (required.isEmpty() || componentBits.and(required) == required)
            && (optional.isEmpty() || !componentBits.and(optional).isEmpty())
            && (excluded.isEmpty() || componentBits.and(excluded).isEmpty())
    }
}
