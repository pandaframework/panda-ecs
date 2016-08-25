package org.pandaframework.ecs.entity

import org.pandaframework.ecs.system.AbstractSystem
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
interface EntitySubscriptionManager: EntityManager {
    fun subscribe(aspect: Aspect): EntitySubscription
    fun edit(entity: Int): EntityEditor

    fun aspectFor(system: KClass<out AbstractSystem>): Aspect

}
