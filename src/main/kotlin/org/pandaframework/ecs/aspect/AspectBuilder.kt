package org.pandaframework.ecs.aspect

import org.pandaframework.ecs.component.Component
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
abstract class AspectBuilder {
    abstract fun <T: Component> allOf(vararg components: KClass<out T>)
    abstract fun <T: Component> anyOf(vararg components: KClass<out T>)
    abstract fun <T: Component> excluding(vararg components: KClass<out T>)

    internal abstract fun build(): Aspect
}
