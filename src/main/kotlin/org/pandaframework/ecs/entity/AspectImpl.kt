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
                _excludeBits = _excludeBits.or(it)
            }
    }
}
