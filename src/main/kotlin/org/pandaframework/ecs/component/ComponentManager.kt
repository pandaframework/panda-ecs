package org.pandaframework.ecs.component

import org.pandaframework.ecs.util.identity.IdentityFactories
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class ComponentManager {
    private val identityFactory = IdentityFactories.basic()
    private val identityCache = mutableMapOf<KClass<out Component>, Int>()

    fun <T: Component> getId(component: KClass<T>): ComponentId {
        return identityCache.getOrPut(component) {
            identityFactory.generate()
        }
    }
}
