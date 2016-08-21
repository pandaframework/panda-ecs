package org.pandaframework.ecs.util

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Ranie Jade Ramiso
 */
class IntBagSpec: SubjectSpek<IntBag>({
    subject { IntBag() }

    describe("insertion") {

        context("inserting an item") {
            beforeEach {
                subject[0] = 3
            }

            it("should increase the size") {
                assertThat(subject.size, equalTo(1))
            }

            it("should insert the item") {
                assertThat(subject[0], equalTo(3))
            }
        }

        context("insertion beyond capacity") {
            beforeEach {
                subject[1] = 4
            }

            it("should insert the item") {
                assertThat(subject[1], equalTo(4))
            }

            it("should flag empty values") {
                assertThat(subject.isEmpty(0), equalTo(true))
            }
        }
    }

    describe("removal") {
        beforeEach {
            subject[0] = 1
            subject[1] = 2
            subject[2] = 3
        }

        context("removing an item") {
            beforeEach {
                subject.remove(0)
            }

            it("should flag removed item as empty") {
                assertThat(subject.isEmpty(0), equalTo(true))
            }

            it("should not affect the size") {
                assertThat(subject.size, equalTo(3))
            }
        }

        it("should throw an exception if removing an empty index") {
            assertThat({
                subject.remove(3)
            }, throws<IndexOutOfBoundsException>())
        }
    }

    it("should throw an exception if accessing an empty index") {
        assertThat({
            subject[0]
        }, throws<IndexOutOfBoundsException>())
    }
})
