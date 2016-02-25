package org.pandaframework.ecs.util.identity

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.shouldBeTrue
import org.jetbrains.spek.api.shouldEqual
import org.jetbrains.spek.api.shouldNotEqual

/**
 * @author Ranie Jade Ramiso
 */
class IdentityFactorySpec : Spek() {
    init {
        given ("a BasicIdentityFactory") {
            val identityFactory = IdentityFactories.basic()
            on("generate") {
                it("should not return negative numbers") {
                    shouldBeTrue(identityFactory.generate() >= 0)
                }
                it("should be unique") {
                    val first = identityFactory.generate()
                    val second = identityFactory.generate()
                    shouldNotEqual(first, second)
                }
            }
        }

        given("a RecyclingIdentityFactory") {
            val identityFactory = IdentityFactories.recycling()
            on("generate") {
                it("should not return negative numbers") {
                    shouldBeTrue(identityFactory.generate() >= 0)
                }
                it("should be unique") {
                    val first = identityFactory.generate()
                    val second = identityFactory.generate()
                    shouldNotEqual(first, second)
                }
            }
            on("free") {
                it("should reuse the freed identity") {
                    val identity = identityFactory.generate()
                    identityFactory.free(identity)
                    shouldEqual(identity, identityFactory.generate())
                }
                it("should reuse the first freed identity") {
                    val identity1 = identityFactory.generate()
                    val identity2 = identityFactory.generate()

                    identityFactory.free(identity1)
                    identityFactory.free(identity2)

                    shouldEqual(identity1, identityFactory.generate())
                }
            }
        }
    }
}
