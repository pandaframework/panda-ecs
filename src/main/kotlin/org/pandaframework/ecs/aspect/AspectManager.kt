package org.pandaframework.ecs.aspect

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentManager
import org.pandaframework.ecs.component.ComponentBits
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class AspectManager(private val componentManager: ComponentManager) {
    fun builder(): AspectBuilder {
        return object: AspectBuilder() {
            private var required = ComponentBits()
            private var optional = ComponentBits()
            private var excluding = ComponentBits()

            override fun <T: Component> allOf(vararg components: KClass<out T>) {
                components.map { componentManager.getId(it) }
                    .forEach { required = required.set(it) }
            }

            override fun <T: Component> anyOf(vararg components: KClass<out T>) {
                components.map { componentManager.getId(it) }
                    .forEach { optional = optional.set(it) }
            }

            override fun <T: Component> excluding(vararg components: KClass<out T>) {
                components.map { componentManager.getId(it) }
                    .forEach { excluding = excluding.set(it) }
            }

            override fun build() = Aspect(required, optional, excluding)
        }
    }
}
