package org.pandaframework.ecs

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.context
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.junit.runner.RunWith

/**
 * @author Ranie Jade Ramiso
 */
@RunWith(JUnitKSpecRunner::class)
class WorldSpec: KSpec() {
    override fun spec() {
        describe(World::class) {
            subject {
                return@subject World(config = worldConfig {
                    systems {
                        +TestSystem1::class
                        +TestSystem2::class
                        +TestSystem3::class
                    }
                })
            }

            context("uninitialized") {
                describe("update") {
                    it("should throw an exception") {
                        assertThat({
                            subject.update(800f)
                        }, throws<IllegalStateException>())
                    }
                }

                describe("destroy") {
                    it("should throw an exception") {
                        assertThat({
                            subject.destroy()
                        }, throws<IllegalStateException>())
                    }
                }

                context("on initialize") {
                    it("should initialize each system") {
                        subject.systemInstances
                                .map { it as TestSystem }
                                .forEach { assertThat(it.initialized, equalTo(true)) }
                    }
                }
            }

            context("initialized") {
                beforeEach { subject.initialize() }
                describe("update") {
                    beforeEach {
                        subject.update(100f)
                    }

                    it("should not process disabled systems") {
                        subject.systemInstances
                                .filter { it is DisabledSystem }
                                .map { it as TestSystem }
                                .forEach { assertThat(it.processed, equalTo(0)) }
                    }

                    it("should not process not processable systems") {
                        subject.systemInstances
                                .filter { it is NotProcessableSystem }
                                .map { it as TestSystem }
                                .forEach { assertThat(it.processed, equalTo(0)) }
                    }

                    it("should process enabled and procesable systems") {
                        subject.systemInstances
                                .map { it as TestSystem }
                                .filter { it.enabled && it.processable }
                                .forEach { assertThat(it.processed, equalTo(1)) }
                    }
                }
            }
        }
    }
}
