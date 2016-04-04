package org.pandaframework.ecs.util.bits

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import io.polymorphicpanda.kspec.KSpec
import io.polymorphicpanda.kspec.describe
import io.polymorphicpanda.kspec.it
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.junit.runner.RunWith

/**
 * @author Ranie Jade Ramiso
 */
@RunWith(JUnitKSpecRunner::class)
class BitsSpec: KSpec() {
    override fun spec() {
        describe("Bits") {
            describe("creation") {
                it("should return the same instance") {
                    assertThat(Bits(), sameInstance(Bits()))
                }
            }

            describe("set") {
                it("should return the same instance") {
                    assertThat(Bits().set(0), sameInstance(Bits().set(0)))
                    assertThat(Bits().set(1), sameInstance(Bits().set(1)))
                    assertThat(Bits().set(1), !sameInstance(Bits().set(0)))
                }
                it("should set the bit to the specified value") {
                    assertThat(Bits().set(0)[0], equalTo(true))
                    assertThat(Bits().set(1)[1], equalTo(true))
                    assertThat(Bits().set(1)[0], equalTo(false))
                }
                it("should not modify original") {
                    val bits = Bits()
                    assertThat(bits, !equalTo(bits.set(0)))
                }
            }

            describe("and") {
                it("should return the same instance") {
                    var first = Bits().set(0)
                    var second = Bits().set(1)
                    assertThat(first.and(second), sameInstance(first.and(second)))
                }
                it("should perform logical and") {
                    val expected = Bits()
                            .set(1)

                    val first = Bits()
                            .set(0)
                            .set(1)
                    val second = Bits()
                            .set(1)
                            .set(1)

                    assertThat(expected, sameInstance(first.and(second)))
                }
                it("should not modify original") {
                    val bits = Bits().set(0)
                    assertThat(bits, !equalTo(bits.and(Bits())))
                }
            }

            describe("or") {
                it("should return the same instance") {
                    var first = Bits().set(0)
                    var second = Bits().set(1)
                    assertThat(first.or(second), sameInstance(first.or(second)))
                }
                it("should perform logical or") {
                    val expected = Bits()
                            .set(0)
                            .set(1)

                    val first = Bits()
                            .set(0)
                            .set(1)
                    val second = Bits()
                            .set(1)
                            .set(1)

                    assertThat(expected, sameInstance(first.or(second)))
                }
                it("should not modify original") {
                    val bits = Bits().set(0)
                    assertThat(bits, !equalTo(bits.or(Bits().set(1))))
                }
            }
        }
    }
}
