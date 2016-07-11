package org.pandaframework.ecs.component

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.it

/**
 * @author Ranie Jade Ramiso
 */
class ComponentIdentityManagerSpec: SubjectSpek<ComponentIdentityManager>({
    subject { DefaultComponentIdentityManager() }
    it("should give unique values") {
        class Component1: Component
        class Component2: Component

        assertThat(subject.getIdentity(Component1::class), !equalTo(subject.getIdentity(Component2::class)))
    }

    it("should return the same value for the same component") {
        class Component1: Component
        assertThat(subject.getIdentity(Component1::class), equalTo(subject.getIdentity(Component1::class)))
    }
})
