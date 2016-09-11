package org.pandaframework.ecs

import org.pandaframework.ecs.component.DefaultComponentFactories
import org.pandaframework.ecs.component.DefaultComponentIdentityManager
import org.pandaframework.ecs.entity.DefaultAspectManager
import org.pandaframework.ecs.entity.DefaultEntitySubscriptionManager
import org.pandaframework.ecs.system.AbstractSystem
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class World private constructor(val systems: LinkedList<KClass<out AbstractSystem>>) {
    private val systemInstances = LinkedList<AbstractSystem>()
    private val componentIdentityManager = DefaultComponentIdentityManager()
    private val componentFactories = DefaultComponentFactories()
    private val aspectManager = DefaultAspectManager(componentIdentityManager)
    private val entitySubscriptionManager = DefaultEntitySubscriptionManager(
        componentIdentityManager, componentFactories
    )

    class Builder {
        val systems = LinkedList<KClass<out AbstractSystem>>()
        fun withSystem(vararg systems: KClass<out AbstractSystem>): Builder {
            this.systems.addAll(systems)
            return this
        }

        fun build(): World {
            return World(systems)
        }
    }

    fun init() {
        systems.map { it.primaryConstructor!!.call()}
            .forEach {
                val aspect = aspectManager.aspectFor(it.javaClass.kotlin)

                it.apply {
                    this.aspect(aspect)
                    _entityManager = entitySubscriptionManager
                    _subscription = entitySubscriptionManager.subscribe(aspect)
                }

                systemInstances.add(it)
            }


        systemInstances.forEach {
            it.init()
        }
    }


    fun update(delta: Double) {

    }

    fun destroy() {
        systemInstances.forEach {
            it.destroy()
        }
    }
}
