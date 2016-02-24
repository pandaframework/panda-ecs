package org.pandaframework.ecs

import org.pandaframework.ecs.entity.EntitySubscriptionManager
import org.pandaframework.ecs.system.AbstractSystem
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.primaryConstructor


fun worldConfig(init: WorldConfig.() -> Unit): WorldConfig {
    val builder = WorldConfig()
    builder.init()
    return builder
}

class WorldConfig internal constructor() {
    internal  val systems: MutableList<KClass<AbstractSystem>> = mutableListOf()

    inner class SystemBuilder internal constructor() {
        operator fun KClass<AbstractSystem>.unaryPlus(){
            if (!isValidSystem(this)) {
                throw IllegalArgumentException("Invalid primary constructor.");
            }

            if (!systems.contains(this)) {
                systems.add(this)
            }
        }

        private fun isValidSystem(cls: KClass<AbstractSystem>): Boolean {
            val primaryConstructor = cls.primaryConstructor
            return primaryConstructor != null
                    && primaryConstructor.parameters.isNotEmpty()
                    && primaryConstructor.isAccessible
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
class World constructor(config: WorldConfig) {
    internal lateinit var entitySubscriptionManager: EntitySubscriptionManager

    val systems: List<KClass<AbstractSystem>> = config.systems

    var initialized: Boolean = false
        private set

    private lateinit var systemInstances: List<AbstractSystem>

    fun initialize() {
        try {
            systemInstances = systems.map {
                (it.primaryConstructor as KFunction<AbstractSystem>).call()
            }

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
        try {
            systemInstances.forEach {
                it.process(delta)
            }
        } catch (e: Throwable) {
            throw WorldException(e);
        }
    }

    fun destroy() {
        try {
            systemInstances.forEach {
                it.destroy()
            }

            initialized = false
        } catch (e: Throwable) {
            throw WorldException(e)
        }
    }
}

class WorldException(message: String?, cause: Throwable?): Throwable(message, cause) {
    constructor(message: String) : this(message, null)
    constructor(cause: Throwable) : this(null, cause)
}


