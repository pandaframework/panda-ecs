package org.pandaframework.ecs

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.DefaultComponentFactories
import org.pandaframework.ecs.component.DefaultComponentIdentityManager
import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.entity.AspectManager
import org.pandaframework.ecs.entity.DefaultAspectManager
import org.pandaframework.ecs.entity.DefaultEntitySubscriptionManager
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.entity.EntitySubscriptionManager
import org.pandaframework.ecs.system.AbstractSystem

/**
 * @author Ranie Jade Ramiso
 */
class MapperSpec: Spek({
    class Component1: Component

    class TestSystem(): AbstractSystem() {
        val mapper: Mapper<Component1> by mapper()

        override fun aspect(aspect: Aspect) {
            aspect.all(Component1::class)
        }

        override fun update(delta: Float) {
        }
    }

    var entityManager: EntitySubscriptionManager?
    var subscription: EntitySubscription?
    var system: TestSystem? = null
    var entity: Int? = null

    beforeEach {
        val componentIdentityManager = DefaultComponentIdentityManager()
        val aspectManager: AspectManager = DefaultAspectManager(componentIdentityManager)
        entityManager = DefaultEntitySubscriptionManager(componentIdentityManager, DefaultComponentFactories())
        system = TestSystem()
        val aspect = aspectManager.aspectFor(system!!)
        subscription = entityManager!!.subscribe(aspect)
        system!!.entityManager = entityManager!!
        system!!.subscription = subscription!!
        system!!.aspect(aspect)


        entity = entityManager!!.create()
    }

    it("should be initially null") {
        assertThat(system!!.mapper.contains(entity!!), equalTo(false))
    }

    it("should return the same instance") {
        val instance = system!!.mapper[entity!!]
        assertThat(instance, sameInstance(system!!.mapper[entity!!]))
    }

    describe("removal") {
        beforeEach {
            // create the instance
            system!!.mapper[entity!!]
        }

        it("it should remove the mapping") {
            system!!.mapper.remove(entity!!)

            assertThat(system!!.mapper.contains(entity!!), equalTo(false))
        }
    }
})
