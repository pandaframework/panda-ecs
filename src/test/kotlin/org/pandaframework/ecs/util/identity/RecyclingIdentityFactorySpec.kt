package org.pandaframework.ecs.util.identity

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.jetbrains.spek.subject.itBehavesLike

/**
 * @author Ranie Jade Ramiso
 */
object RecyclingIdentityFactorySpec: SubjectSpek<RecyclingIdentityFactory>({
    subject { IdentityFactories.recycling() }

    itBehavesLike(IdentityFactorySpec)

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
})
