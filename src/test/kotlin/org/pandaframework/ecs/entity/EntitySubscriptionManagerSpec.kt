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
import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
class EntitySubscriptionManagerSpec: SubjectSpek<EntitySubscriptionManager>({

    subject {
        DefaultEntitySubscriptionManager(DefaultComponentIdentityManager(), DefaultComponentFactories())
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
                assertThat(subject.edit(entity).hasComponent(Component1::class), equalTo(true))
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

            override fun update(delta: Float) {
                // nada
            }
        }

        var subscription: EntitySubscription? = null
        var entity: Int = 0

        beforeEach {
            val aspectManager = DefaultAspectManager(DefaultComponentIdentityManager())
            // setup internal state
            val aspect = aspectManager.aspectFor(System1::class)
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

        describe("listeners") {
            var listener: EntitySubscription.Listener? = null
            val added = LinkedList<Int>()
            val removed = LinkedList<Int>()

            beforeEach {
                listener = object: EntitySubscription.Listener {
                    override fun entityAdded(entity: Int) {
                        added.add(entity)
                    }

                    override fun entityRemoved(entity: Int) {
                        removed.add(entity)
                    }

                }
                subscription!!.addListener(listener!!)
            }

            it("should notify when an entity is added") {
                val editor = subject.edit(entity)
                editor.addComponent(Component1::class)
                editor.addComponent(Component2::class)

                assertThat(added.contains(entity), equalTo(true))
            }

            it("should notify when an entity is removed") {
                val editor = subject.edit(entity)
                editor.addComponent(Component1::class)
                editor.addComponent(Component2::class)

                editor.removeComponent(Component1::class)

                assertThat(removed.contains(entity), equalTo(true))
            }

            afterEach {
                subscription!!.removeListener(listener!!)
                added.clear()
                removed.clear()
            }
        }
    }
})
