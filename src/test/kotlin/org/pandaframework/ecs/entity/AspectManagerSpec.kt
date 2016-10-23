package org.pandaframework.ecs.entity

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.it
import org.pandaframework.ecs.component.DefaultComponentIdentityManager
import org.pandaframework.ecs.system.AbstractSystem

/**
 * @author Ranie Jade Ramiso
 */
internal class AspectManagerSpec: SubjectSpek<AspectManager>({
    subject { DefaultAspectManager(DefaultComponentIdentityManager()) }

    it("should return the same instance") {
        class System(): AbstractSystem() {
            override fun aspect(aspect: Aspect) {
                // nada
            }

            override fun update(delta: Float) {
                // nada
            }
        }

        val system = System()

        assertThat(subject.aspectFor(system), sameInstance(subject.aspectFor(system)))
    }
})
