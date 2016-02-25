package org.pandaframework.ecs.util.bits

import org.jetbrains.spek.api.*

/**
 * @author Ranie Jade Ramiso
 */
class BitsSpec: Spek() {
    init {
        given("a Bits") {
            on("creation") {
                it("should return the same instance") {
                    shouldBeTrue(Bits() === Bits())
                }
            }

            on("set") {
                it("should return the same instance") {
                    shouldEqual(Bits().set(0), Bits().set(0))
                    shouldEqual(Bits().set(1), Bits().set(1))
                }
                it("should set the bit to the specified value") {
                    shouldBeTrue(Bits().set(0)[0])
                    shouldBeTrue(Bits().set(1)[1])
                    shouldBeFalse(Bits().set(1)[0])

                    val bits = Bits().set(1)
                    shouldBeFalse(bits.set(0, false)[0])
                }
                it("should not modify original") {
                    val bits = Bits()
                    shouldNotEqual(bits, bits.set(0))
                }
            }

            on("and") {
                it("should return the same instance") {
                    var first = Bits().set(0)
                    var second = Bits().set(1)
                    shouldEqual(first.and(second), first.and(second))
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

                    shouldEqual(expected, first.and(second))
                }
                it("should not modify original") {
                    val bits = Bits().set(0)
                    shouldNotEqual(bits, bits.and(Bits()))
                }
            }

            on("or") {
                it("should return the same instance") {
                    var first = Bits().set(0)
                    var second = Bits().set(1)
                    shouldEqual(first.or(second), first.or(second))
                }
                it("should perform logical and") {
                    val expected = Bits()
                            .set(0)
                            .set(1)

                    val first = Bits()
                            .set(0)
                            .set(1)
                    val second = Bits()
                            .set(1)
                            .set(1)

                    shouldEqual(expected, first.or(second))
                }
                it("should not modify original") {
                    val bits = Bits().set(0)
                    shouldNotEqual(bits, bits.or(Bits().set(1)))
                }
            }
        }
    }
}
