package org.pandaframework.ecs.entity.pool

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.component.ComponentManager

/**
 * @author Ranie Jade Ramiso
 */
object BasicEntityPoolSpec: SubjectSpek<BasicEntityPool>({
    val componentManager = memoized { ComponentManager() }
    subject { BasicEntityPool(componentManager()) }

    describe("creating entities") {
        it("there should be no duplicates") {
            expect(subject.create()).not.equal(subject.create())
        }
    }

    describe("editing entities") {
        it("should return the same editor instance") {
            val entity = subject.create()
            expect(subject.edit(entity)).satisfy {
                it === subject.edit(entity)
            }
        }
    }


    describe("destroying entities") {
        it("should only destroy if entity exists") {
            assertThat({
                subject.destroy(666)
            }, throws<IllegalArgumentException>())
        }
    }
})
