package org.pandaframework.ecs.component

import org.pandaframework.ecs.util.pool.BasicObjectPool
import org.pandaframework.ecs.util.pool.ObjectPool
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class DefaultComponentFactories: ComponentFactories {
    private val factories: MutableMap<KClass<out Component>, ComponentFactory<*>> = HashMap()

    override fun <T: Component> factoryFor(component: KClass<T>): ComponentFactory<T> {
        return factories.getOrPut(component) {
            object: ComponentFactory<T> {
                val pool: ObjectPool<T> = BasicObjectPool(10, {
                    component.primaryConstructor!!.call()
                })

                override fun create(): T {
                    return pool.acquire()
                }

                override fun release(instance: T) {
                    return pool.release(instance)
                }

            }
        } as ComponentFactory<T>
    }
}
