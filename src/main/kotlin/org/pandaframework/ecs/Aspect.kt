package org.pandaframework.ecs

import org.pandaframework.ecs.component.Component
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface Aspect {
    fun all(): Id
    fun any(): Id
    fun excluded(): Id
}


fun aspect(block: AspectBuilder.() -> Unit): Aspect {
    return AspectBuilder()
        .apply(block)
        .build()
}

class AspectBuilder {

    fun all(vararg components: KClass<out Component>): AspectBuilder {
        return this
    }

    fun any(vararg components: KClass<out Component>): AspectBuilder {
        return this
    }

    fun exclude(vararg components: KClass<out Component>): AspectBuilder {
        return this
    }

    fun build(): Aspect {
        TODO()
    }
}
