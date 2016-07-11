package org.pandaframework.ecs.entity

import org.pandaframework.ecs.util.IntBag
import org.pandaframework.ecs.util.identity.IdentityFactories
import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
class DefaultEntitySubscriptionManager: EntitySubscriptionManager {
    val identityFactory = IdentityFactories.recycling()
    val entities = IntBag()
    val aspects = HashMap<AspectImpl, IntBag>()

    override fun subscribe(aspect: Aspect): EntitySubscription {
        TODO()
    }

    override fun create(): Int {
        TODO()
    }

    override fun remove(entity: Int) {
        TODO()
    }

    override fun edit(entity: Int): EntityEditor {
        TODO()
    }

}
