package org.pandaframework.ecs

import org.pandaframework.ecs.component.DefaultComponentFactories
import org.pandaframework.ecs.component.DefaultComponentIdentityManager
import org.pandaframework.ecs.entity.DefaultAspectManager
import org.pandaframework.ecs.entity.DefaultEntitySubscriptionManager
import org.pandaframework.ecs.system.AbstractSystem
import java.util.LinkedList

/**
 * @author Ranie Jade Ramiso
 */
class World private constructor(val systems: LinkedList<AbstractSystem>) {
    private val componentIdentityManager = DefaultComponentIdentityManager()
    private val componentFactories = DefaultComponentFactories()
    private val aspectManager = DefaultAspectManager(componentIdentityManager)
    private val entitySubscriptionManager = DefaultEntitySubscriptionManager(
        componentIdentityManager, componentFactories
    )

    class Builder {
        private val systems = LinkedList<AbstractSystem>()

        fun withSystem(vararg systems: AbstractSystem): Builder {
            this.systems.addAll(systems)
            return this
        }

        fun build(): World {
            return World(systems)
        }
    }

    fun init() {
        systems.forEach {
            val aspect = aspectManager.aspectFor(it)
            it.entityManager = entitySubscriptionManager
            it.subscription = entitySubscriptionManager.subscribe(aspect)
            it.aspect(aspect)
            it.init()
        }
    }


    fun update(delta: Float) {
        systems.forEach {
            it.update(delta)
        }
    }

    fun destroy() {
        systems.forEach(AbstractSystem::destroy)
    }
}
