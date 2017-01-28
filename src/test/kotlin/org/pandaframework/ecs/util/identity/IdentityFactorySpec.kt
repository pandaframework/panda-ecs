package org.pandaframework.ecs.util.identity

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThanOrEqualTo
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

/**
 * @author Ranie Jade Ramiso
 */
object IdentityFactorySpec : SubjectSpek<IdentityFactory>({
    subject { IdentityFactories.basic() }
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
})
