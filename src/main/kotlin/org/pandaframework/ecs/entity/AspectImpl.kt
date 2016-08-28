package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.util.Bits
import kotlin.reflect.KClass

class AspectImpl(private val componentIdentityManager: ComponentIdentityManager): Aspect {
    val allBits: Bits
        get() = _allBits
    val anyBits: Bits
        get() = _anyBits
    val excludeBits: Bits
        get() = _excludeBits

    private var _allBits = Bits()
    private var _anyBits = Bits()
    private var _excludeBits = Bits()

    override fun all(vararg components: KClass<out Component>): Aspect {
        components.map { componentIdentityManager.getIdentity(it) }
            .forEach {
                _allBits = _allBits.or(it)
            }

        return this
    }

    override fun any(vararg components: KClass<out Component>): Aspect {
        components.map { componentIdentityManager.getIdentity(it) }
            .forEach {
                _anyBits = _anyBits.or(it)
            }

        return this
    }

    override fun exclude(vararg components: KClass<out Component>): Aspect {
        components.map { componentIdentityManager.getIdentity(it) }
            .forEach {
                _excludeBits = _excludeBits.or(it)
            }

        return this
    }


    fun match(bits: Bits): Boolean {
        return (allBits.isEmpty() || bits.and(allBits) == allBits) &&
            (anyBits.isEmpty() || !bits.and(anyBits).isEmpty()) &&
            (excludeBits.isEmpty() || bits.and(excludeBits).isEmpty())
    }
}
