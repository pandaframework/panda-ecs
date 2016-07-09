package org.pandaframework.ecs.component

import org.pandaframework.ecs.util.Bits
import org.pandaframework.ecs.util.identity.IdentityFactories
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class DefaultComponentIdentityManager: ComponentIdentityManager {
    private val identityFactory = IdentityFactories.basic()
    private val identityMap = HashMap<KClass<out Component>, Bits>()

    override fun getIdentity(component: KClass<out Component>): Bits {
        return identityMap.computeIfAbsent(component, {
            Bits().set(identityFactory.generate())
        })
    }
}
