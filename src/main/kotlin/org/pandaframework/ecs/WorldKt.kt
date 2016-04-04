package org.pandaframework.ecs

import org.pandaframework.ecs.entity.EntitySubscriptionManager
import org.pandaframework.ecs.system.AbstractSystem
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.primaryConstructor


fun worldConfig(init: WorldConfig.() -> Unit): WorldConfig {
    val builder = WorldConfig()
    builder.init()
    return builder
}

class WorldConfig internal constructor() {
    internal  val systems: MutableList<KClass<out AbstractSystem>> = mutableListOf()

    inner class SystemBuilder internal constructor() {
        operator fun KClass<out AbstractSystem>.unaryPlus(){
            if (!isValidSystem(this)) {
                throw IllegalArgumentException("Invalid primary constructor.");
            }

            if (!systems.contains(this)) {
                systems.add(this)
            }
        }

        private fun isValidSystem(cls: KClass<out AbstractSystem>): Boolean {
            val primaryConstructor = cls.primaryConstructor
            return primaryConstructor != null
                    && primaryConstructor.parameters.isEmpty()
        }
    }


    fun systems(init: SystemBuilder.() -> Unit): SystemBuilder {
        val systems = SystemBuilder()
        systems.init()
        return systems
    }
}

/**
 * @author Ranie Jade Ramiso
 */
class World constructor(config: WorldConfig = worldConfig {}) {
    internal val entitySubscriptionManager: EntitySubscriptionManager = EntitySubscriptionManager()
    internal val systemInstances = LinkedList<AbstractSystem>()

    val systems: List<KClass<out AbstractSystem>> = config.systems

    var initialized: Boolean = false
        private set

    fun initialize() {
        try {
            systemInstances.addAll(systems.map {
                (it.primaryConstructor as KFunction<AbstractSystem>).call()
            })

            systemInstances.forEach {
                it.world = this
                it.initialize()
            }

            initialized = true

        } catch (e: WorldException) {
            throw e
        } catch (e: Throwable) {
            throw WorldException(e)
        }
    }

    fun update(delta: Float) {
        require(delta > 0)
        assertInitialized()

        try {
            systemInstances.forEach {

                if (it.enabled && it.canProcess(delta)) {
                    it.process(delta)
                }
            }
        } catch (e: Throwable) {
            throw WorldException(e);
        }
    }

    fun destroy() {
        assertInitialized()
        try {
            systemInstances.forEach {
                it.destroy()
            }

            initialized = false
        } catch (e: Throwable) {
            throw WorldException(e)
        }
    }

    private fun assertInitialized() {
        if (!initialized) {
            error("World must be initialized first")
        }
    }
}

class WorldException(message: String?, cause: Throwable?): Throwable(message, cause) {
    constructor(message: String) : this(message, null)
    constructor(cause: Throwable) : this(null, cause)
}


