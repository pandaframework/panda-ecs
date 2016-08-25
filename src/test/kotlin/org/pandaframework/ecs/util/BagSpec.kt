package org.pandaframework.ecs.util

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * @author Ranie Jade Ramiso
 */
class BagSpec: SubjectSpek<Bag<Int>>({
    subject { Bag<Int>() }

    describe("insertion") {

        on("inserting an item") {
            beforeEach {
                subject[0] = 3
            }

            it("should increase the size") {
                assertThat(subject.size, equalTo(1))
            }

            it("should insert the item") {
                assertThat(subject[0], present(equalTo(3)))
            }
        }

        on("insertion beyond capacity") {
            beforeEach {
                subject[1] = 4
            }

            it("should insert the item") {
                assertThat(subject[1], present(equalTo(4)))
            }

            it("should set to null empty indexes") {
                assertThat(subject[0], absent())
            }
        }
    }

    describe("removal") {
        beforeEach {
            subject[0] = 1
            subject[1] = 2
            subject[2] = 3
        }

        on("removing an item") {
            beforeEach {
                subject.remove(0)
            }

            it("should set the removed item to null") {
                assertThat(subject[0], absent())
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

    describe("isPresent") {
        beforeEach {
            subject[0] = 1
        }

        it("should return true if value is present") {
            assertThat(subject.isPresent(0), equalTo(true))
        }

        it("should return false if value is not present") {
            assertThat(subject.isPresent(1), equalTo(false))
        }
    }

    it("should return null if accessing an empty index") {
        assertThat(subject[0], absent())
    }
})
