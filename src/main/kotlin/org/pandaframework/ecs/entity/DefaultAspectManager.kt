package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.system.AbstractSystem
import org.pandaframework.ecs.util.Bits
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal class DefaultAspectManager(val componentIdentityManager: ComponentIdentityManager): AspectManager {
    val aspectMap = HashMap<KClass<out AbstractSystem>, Aspect>()

    override fun aspectFor(system: KClass<out AbstractSystem>): Aspect {
        return aspectMap.computeIfAbsent(system, { AspectImpl() })
    }


    inner class AspectImpl: Aspect {
        override val allBits: Bits
            get() = _allBits
        override val anyBits: Bits
            get() = _anyBits
        override val excludeBits: Bits
            get() = _excludeBits

        var _allBits = Bits()
        var _anyBits = Bits()
        var _excludeBits = Bits()

        override fun all(vararg components: KClass<out Component>) {
            components.map { componentIdentityManager.getIdentity(it) }
                .forEach {
                    _allBits = _allBits.or(it)
                }
        }

        override fun any(vararg components: KClass<out Component>) {
            components.map { componentIdentityManager.getIdentity(it) }
                .forEach {
                    _anyBits = _anyBits.or(it)
                }
        }

        override fun exclude(vararg components: KClass<out Component>) {
            components.map { componentIdentityManager.getIdentity(it) }
                .forEach {
                    _excludeBits = excludeBits.or(it)
                }
        }
    }
}
