package org.pandaframework.ecs.component

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.polymorphicpanda.kspec.*
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.junit.runner.RunWith

/**
 * @author Ranie Jade Ramiso
 */
@RunWith(JUnitKSpecRunner::class)
class DefaultComponentManagerSpec: KSpec() {
    override fun spec() {
        describe(DefaultComponentIdentityManager::class) {
            itBehavesLike(componentIdentityManager())
        }
    }

    companion object {
        fun componentIdentityManager() = sharedExample<ComponentIdentityManager> {
            it("should give unique values") {
                class Component1: Component
                class Component2: Component

                assertThat(subject.getIdentity(Component1::class), !equalTo(subject.getIdentity(Component2::class)))
            }

            it("should return the same value for the same component") {
                class Component1: Component
                assertThat(subject.getIdentity(Component1::class), equalTo(subject.getIdentity(Component1::class)))
            }
        }
    }
}
