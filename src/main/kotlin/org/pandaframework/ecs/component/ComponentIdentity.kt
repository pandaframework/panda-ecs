package org.pandaframework.ecs.component

import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal data class ComponentIdentity<T: Component>(val identity: Int, val component: KClass<T>) {
}
