package org.pandaframework.ecs.system

import org.pandaframework.ecs.Mapper
import org.pandaframework.ecs.MapperDelegate
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.EntitySubscription
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSystem() {
    internal lateinit var entityManager: EntityManager
    internal lateinit var subscription: EntitySubscription

    abstract fun aspect(aspect: Aspect)

    open fun init() { }

    abstract fun update(delta: Float)

    open fun destroy() { }

    protected inline fun <reified T: Component> mapper() = mapperFor(T::class)

    protected fun <T: Component> mapperFor(component: KClass<T>) = object: MapperDelegate<T> {
        val mapper: Mapper<T> by lazy {
            entityManager.mapper(component)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): Mapper<T> = mapper

    }
}
