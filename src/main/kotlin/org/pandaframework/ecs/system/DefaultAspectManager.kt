package org.pandaframework.ecs.system

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.util.Bits
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class DefaultAspectManager(val componentIdentityManager: ComponentIdentityManager): AspectManager {
    val aspectMap = HashMap<KClass<out AbstractSystem>, Aspect>()

    override fun aspectFor(system: KClass<out AbstractSystem>): Aspect {
        return aspectMap.computeIfAbsent(system, { AspectImpl() })
    }


    inner class AspectImpl: Aspect {
        var allBits = Bits()
        var anyBits = Bits()
        var excludeBits = Bits()

        val matcher = object: Aspect.Matcher {
            override fun matches(composition: Bits): Boolean {
                return composition.and(allBits) == allBits
                    && composition.and(anyBits) != Bits()
                    && composition.and(excludeBits) == Bits()
            }

        }

        override fun all(vararg components: KClass<out Component>) {
            components.map { componentIdentityManager.getIdentity(it) }
                .forEach {
                    allBits = allBits.or(it)
                }
        }

        override fun any(vararg components: KClass<out Component>) {
            components.map { componentIdentityManager.getIdentity(it) }
                .forEach {
                    anyBits = anyBits.or(it)
                }
        }

        override fun exclude(vararg components: KClass<out Component>) {
            components.map { componentIdentityManager.getIdentity(it) }
                .forEach {
                    excludeBits = excludeBits.or(it)
                }
        }

        override fun matcher() = matcher

    }
}
