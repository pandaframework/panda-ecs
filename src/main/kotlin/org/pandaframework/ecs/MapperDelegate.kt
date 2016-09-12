package org.pandaframework.ecs

import org.pandaframework.ecs.component.Component
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
interface MapperDelegate<out T: Component> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Mapper<T>
}
