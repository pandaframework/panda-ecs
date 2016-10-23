package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.system.AbstractSystem
import java.util.HashMap

/**
 * @author Ranie Jade Ramiso
 */
internal class DefaultAspectManager(val componentIdentityManager: ComponentIdentityManager): AspectManager {
    val aspectMap = HashMap<AbstractSystem, AspectImpl>()

    override fun aspectFor(system: AbstractSystem): AspectImpl {
        return aspectMap.computeIfAbsent(system, {
            val aspect = AspectImpl(componentIdentityManager)
            aspect
        })
    }
}
