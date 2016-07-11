package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.system.AbstractSystem
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal class DefaultAspectManager(val componentIdentityManager: ComponentIdentityManager): AspectManager {
    val aspectMap = HashMap<KClass<out AbstractSystem>, Aspect>()

    override fun aspectFor(system: KClass<out AbstractSystem>): Aspect {
        return aspectMap.computeIfAbsent(system, { AspectImpl(componentIdentityManager) })
    }


}
