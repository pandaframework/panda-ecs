package org.pandaframework.ecs

import org.pandaframework.ecs.aspect.Aspect
import org.pandaframework.ecs.aspect.AspectBuilder
import org.pandaframework.ecs.aspect.AspectManager
import org.pandaframework.ecs.component.ComponentManager


fun ComponentManager.aspect(block: AspectBuilder.() -> Unit): Aspect {
    val builder = AspectManager(this).builder()
    block.invoke(builder)
    return builder.build()
}

fun ComponentManager.defaultAspect() = aspect {  }
