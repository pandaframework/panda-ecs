package org.pandaframework.ecs.component

import org.pandaframework.ecs.util.Bits
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal interface ComponentIdentityManager {
    fun getIdentity(component: KClass<out Component>): Bits
}
