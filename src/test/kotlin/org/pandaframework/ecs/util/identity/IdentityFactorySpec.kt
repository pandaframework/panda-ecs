package org.pandaframework.ecs.util.identity

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.greaterThanOrEqualTo
import io.polymorphicpanda.kspec.*
import io.polymorphicpanda.kspec.junit.JUnitKSpecRunner
import org.junit.runner.RunWith

/**
 * @author Ranie Jade Ramiso
 */
@RunWith(JUnitKSpecRunner::class)
class IdentityFactorySpec : KSpec() {
    override fun spec() {
        describe(IdentityFactory::class) {
            subject {
                return@subject IdentityFactories.basic()
            }

            itBehavesLike(identityFactory())
        }

        describe(RecyclingIdentityFactory::class) {
            subject {
                return@subject IdentityFactories.recycling()
            }

            itBehavesLike(identityFactory())

            describe("free") {
                it("should reuse the freed identity") {
                    val identity = subject.generate()
                    subject.free(identity)
                    assertThat(identity, equalTo(subject.generate()))
                }
                it("should reuse the first freed identity") {
                    val identity1 = subject.generate()
                    val identity2 = subject.generate()

                    subject.free(identity1)
                    subject.free(identity2)

                    assertThat(identity1, equalTo(subject.generate()))
                }
            }
        }
    }

    companion object {
        fun identityFactory() = sharedExample<IdentityFactory> {
            describe("generate") {
                it("should not return negative numbers") {
                    assertThat(subject.generate(), greaterThanOrEqualTo(0))
                }

                it("should generate unique ids") {
                    val first = subject.generate()
                    val second = subject.generate()
                    assertThat(first, !equalTo(second))
                }
            }
        }
    }
}
