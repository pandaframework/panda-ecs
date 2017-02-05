package org.pandaframework.ecs.entity

import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.aspect
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentManager
import org.pandaframework.ecs.defaultAspect
import org.pandaframework.ecs.entity.pool.BasicEntityPool
import org.pandaframework.ecs.entity.pool.EntityPool

object SubscriptionSpec: SubjectSpek<EntityPool>({
    val componentManager = memoized { ComponentManager() }
    subject { BasicEntityPool(componentManager()) }

    class Component1: Component
    class Component2: Component
    class Component3: Component

    context("subscription with required components") {
        val entity = memoized {
            subject.create()
        }

        beforeEachTest {
            val editor = subject.edit(entity())
            editor.get(Component1::class)
            editor.get(Component2::class)
        }

        it("entity should match allOf(Component1)") {
            val subscription = subject.subscribe(componentManager().aspect {
                allOf(Component1::class, Component2::class)
            })

            expect(subscription.entities().contains(entity())).to.be.`true`
        }

        it("entity should match allOf(Component1, Component2)") {
            val subscription = subject.subscribe(componentManager().aspect {
                allOf(Component1::class, Component2::class)
            })
            expect(subscription.entities().contains(entity())).to.be.`true`
        }

        it("entity should not match allOf(Component1, Component2, Component3)") {
            val subscription = subject.subscribe(componentManager().aspect {
                allOf(Component1::class, Component2::class, Component3::class)
            })
            expect(subscription.entities().contains(entity())).to.be.`false`
        }
    }

    context("subscription with optional components") {
        val entity1 = memoized {
            subject.create()
        }

        val entity2 = memoized {
            subject.create()
        }

        beforeEachTest {
            subject.edit(entity1()).apply {
                get(Component1::class)
                get(Component2::class)
            }

            subject.edit(entity2()).apply {
                get(Component1::class)
                get(Component3::class)
            }
        }

        it("entity1 and entity2 should match allOf(Component1) anyOf(Component2, Component3)") {
            val subscription = subject.subscribe(componentManager().aspect {
                anyOf(Component2::class, Component3::class)
            })
            expect(subscription.entities().contains(entity1())).to.be.`true`
            expect(subscription.entities().contains(entity2())).to.be.`true`
        }

        it("entity1 should not match allOf(Component1) anyOf(Component3)") {
            val subscription = subject.subscribe(componentManager().aspect {
                anyOf(Component3::class)
            })
            expect(subscription.entities().contains(entity1())).to.be.`false`
        }

        it("entity2 should not match allOf(Component1) anyOf(Component2)") {
            val subscription = subject.subscribe(componentManager().aspect {
                anyOf(Component2::class)
            })
            expect(subscription.entities().contains(entity2())).to.be.`false`
        }
    }

    context("subscription with excluded components") {
        val entity1 = memoized {
            subject.create()
        }

        val entity2 = memoized {
            subject.create()
        }

        beforeEachTest {
            subject.edit(entity1()).apply {
                get(Component1::class)
                get(Component2::class)
            }

            subject.edit(entity2()).apply {
                get(Component1::class)
                get(Component3::class)
            }
        }

        it("entity1 should match excluding(Component3)") {
            val subscription = subject.subscribe(componentManager().aspect {
                excluding(Component3::class)
            })
            expect(subscription.entities().contains(entity1())).to.be.`true`
        }

        it("entity2 should match excluding(Component2)") {
            val subscription = subject.subscribe(componentManager().aspect {
                excluding(Component2::class)
            })
            expect(subscription.entities().contains(entity2())).to.be.`true`
        }

        it("entity1 and entity2 should not matching excluding(Component1)") {
            val subscription = subject.subscribe(componentManager().aspect {
                excluding(Component1::class)
            })
            expect(subscription.entities().contains(entity1())).to.be.`false`
            expect(subscription.entities().contains(entity2())).to.be.`false`
        }
    }

    context("default subscription") {
        val entity1 = memoized {
            subject.create()
        }

        val entity2 = memoized {
            subject.create()
        }

        val entity3 = memoized {
            subject.create()
        }


        beforeEachTest {
            subject.edit(entity1()).apply {
                get(Component1::class)
                get(Component2::class)
            }

            subject.edit(entity2()).apply {
                get(Component1::class)
                get(Component3::class)
            }

            // create entity3
            entity3()
        }
        it("should contain all entities regardless of what components they have") {
            val subscription = subject.subscribe(componentManager().defaultAspect())
            expect(subscription.entities().contains(entity1())).to.be.`true`
            expect(subscription.entities().contains(entity2())).to.be.`true`
            expect(subscription.entities().contains(entity3())).to.be.`true`
        }
    }

    describe("listeners") {
        val entity1 = memoized {
            subject.create()
        }

        val entity2 = memoized {
            subject.create()
        }

        val entity3 = memoized {
            subject.create()
        }

        val listener = memoized {
            object: EntitySubscriptionListener {
                val added = mutableSetOf<Entity>()
                val removed = mutableSetOf<Entity>()

                override fun added(entity: Entity) {
                    added.add(entity)
                }

                override fun removed(entity: Entity) {
                    removed.add(entity)
                }

            }
        }


        beforeEachTest {
            subject.edit(entity1()).apply {
                get(Component1::class)
                get(Component2::class)
            }

            subject.edit(entity2()).apply {
                get(Component1::class)
                get(Component3::class)
            }

            // create entity3
            entity3()

            val subscription = subject.subscribe(componentManager().aspect {
                allOf(Component1::class)
            })

            subscription.addListener(listener())
        }

        it("should notify added entities") {
            subject.edit(entity3()).apply {
                get(Component1::class)
            }

            expect(listener().added).contain(entity3())
        }

        it("should notify removed entities") {
            subject.edit(entity1()).apply {
                remove(Component1::class)
            }

            expect(listener().removed).contain(entity1())
        }
    }
})
