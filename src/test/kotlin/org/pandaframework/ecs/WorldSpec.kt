package org.pandaframework.ecs.util.org.pandaframework.ecs

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.shouldBeTrue
import org.jetbrains.spek.api.shouldEqual
import org.jetbrains.spek.api.shouldThrow
import org.pandaframework.ecs.World
import org.pandaframework.ecs.worldConfig

/**
 * @author Ranie Jade Ramiso
 */
class WorldSpec: Spek() {
    init {
        given("an uninitialized World") {
            val world = World(worldConfig {
                systems {
                    + TestSystem1::class
                    + TestSystem2::class
                    + TestSystem3::class
                }
            })

            on("update") {
                it("should throw an exception") {
                    shouldThrow(IllegalStateException::class.java, {world.update(800f)})
                }
            }

            on("destroy") {
                it("should throw an exception") {
                    shouldThrow(IllegalStateException::class.java, {world.destroy()})
                }
            }
        }

        given("another uninitiliazed World cause Spek sucks") {
            val world = World(worldConfig {
                systems {
                    + TestSystem1::class
                    + TestSystem2::class
                    + TestSystem3::class
                }
            })

            on("initialize") {
                world.initialize()

                it("should initialize each system") {
                    world.systemInstances
                        .map { it as TestSystem }
                        .forEach { shouldBeTrue(it.initialized) }
                }
            }
        }

        given("an initialized World") {
            val world = World(worldConfig {
                systems {
                    + TestSystem1::class
                    + TestSystem2::class
                    + TestSystem3::class
                    + DisabledSystem::class
                    + NotProcessableSystem::class
                }
            })

            world.initialize()

            on("update") {
                world.update(100f)

                it("should not process disabled systems") {
                    world.systemInstances
                        .filter { it is DisabledSystem }
                        .map { it as TestSystem }
                        .forEach { shouldEqual(0, it.processed) }
                }

                it("should not process not processable systems") {
                    world.systemInstances
                            .filter { it is NotProcessableSystem }
                            .map { it as TestSystem }
                            .forEach { shouldEqual(0, it.processed) }
                }

                it("should process enabled and procesable systems") {
                    world.systemInstances
                            .map { it as TestSystem }
                            .filter { it.enabled && it.processable }
                            .forEach { shouldEqual(1, it.processed) }
                }
            }

            on("update with an invalid delta") {
                it("should throw an exception") {
                    shouldThrow(IllegalArgumentException::class.java, {world.update(0f)})
                    shouldThrow(IllegalArgumentException::class.java, {world.update(-1f)})
                }
            }

            on("destroy") {
                world.destroy()

                it("should destroy each system") {
                    world.systemInstances
                        .map { it as TestSystem }
                        .forEach { shouldBeTrue(it.destroyed) }
                }
            }
        }
    }
}
