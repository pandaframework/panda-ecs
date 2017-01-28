package org.pandaframework.ecs.entity.pool

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.component.ComponentManager

/**
 * @author Ranie Jade Ramiso
 */
object BasicEntityPoolSpec: SubjectSpek<BasicEntityPool>({
    val componentManager = memoized { ComponentManager() }
    subject { BasicEntityPool(componentManager()) }

    context("creating entities") {
        it("there should be no duplicates") {
            assertThat(subject.create(), !equalTo(subject.create()))
        }
    }

    context("destroying entities") {
        it("should only destroy if entity exists") {
            assertThat({
                subject.destroy(666)
            }, throws<IllegalArgumentException>())
        }
    }
})
