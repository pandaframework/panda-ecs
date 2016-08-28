package org.pandaframework.ecs.entity

import com.natpryce.hamkrest.anything
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.dsl.xdescribe
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.DefaultComponentFactories
import org.pandaframework.ecs.component.DefaultComponentIdentityManager
import org.pandaframework.ecs.system.AbstractSystem

/**
 * @author Ranie Jade Ramiso
 */
class EntitySubscriptionManagerSpec: SubjectSpek<EntitySubscriptionManager>({

    subject {
        DefaultEntitySubscriptionManager(DefaultComponentIdentityManager(), DefaultComponentFactories())
    }

    describe("entity creation") {
        it("should return unique ids") {
            assertThat(subject.create(), !equalTo(subject.create()))
        }
    }

    describe("entity removal") {
        it("should throw an exception if entity is not alive") {
            assertThat({
                subject.remove(1)
            }, throws<EntityNotFoundException>())
        }

        it("should delete alive entities") {
            val entity = subject.create()
            assertThat({
                subject.remove(entity)
            }, !throws<EntityNotFoundException>())
        }
    }

    describe("entity edit") {
        it("should throw an exception if entity is not alive") {
            assertThat({
                subject.edit(1)
            }, throws<EntityNotFoundException>())
        }

        it("should delete alive entities") {
            val entity = subject.create()
            assertThat({
                subject.edit(entity)
            }, !throws<EntityNotFoundException>())
        }

        on("adding a component") {
            var entity: Int = 0
            class Component1: Component

            beforeEach {
                entity = subject.create()
                subject.edit(entity).addComponent(Component1::class)
            }

            it("should attach the component instance to that entity") {
                assertThat(subject.edit(entity).getComponent(Component1::class), present(anything))
            }
        }
    }

    describe("entity subscriptions") {
        class Component1: Component
        class Component2: Component

        class System1: AbstractSystem() {
            override fun aspect(aspect: Aspect) {
                // aspect.all(Component1::class, Component2::class)
            }

            override fun update(delta: Float, entities: IntArray) {
                // nada
            }
        }

        var subscription: EntitySubscription? = null
        var entity: Int = 0

        beforeEach {
            // setup internal state
            val aspect = subject.aspectFor(System1::class)
            aspect.all(Component1::class, Component2::class)
            subscription = subject.subscribe(aspect)
            entity = subject.create()
        }

        on("entity edit") {
            beforeEach {
                val editor = subject.edit(entity)
                editor.addComponent(Component1::class)
                editor.addComponent(Component2::class)
            }

            it("should contain all entities matching the aspect") {
                val entities = subscription!!.entities()
                assertThat(entities.size, !equalTo(0))
                assertThat(entities[0], equalTo(entity))
            }
        }

        xdescribe("listeners") {

        }
    }
})
