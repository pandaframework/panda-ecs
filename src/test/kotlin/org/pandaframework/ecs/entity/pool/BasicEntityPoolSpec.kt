package org.pandaframework.ecs.entity.pool

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.aspect.Aspect
import org.pandaframework.ecs.component.ComponentManager
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.util.Bits
import kotlin.properties.Delegates

/**
 * @author Ranie Jade Ramiso
 */
object BasicEntityPoolSpec: SubjectSpek<BasicEntityPool>({
    subject { BasicEntityPool(ComponentManager()) }

    on("Creating entities") {
        val entity1 = subject.create()
        val entity2 = subject.create()
        val subscription = subject.subscribe(Aspect(Bits(), Bits(), Bits()))

        it("should be in subscription") {
            assertThat(subscription.entities().contains(entity1), equalTo(true))
            assertThat(subscription.entities().contains(entity2), equalTo(true))
        }
    }

    describe("Destroying entities") {
        var entity by Delegates.notNull<Int>()
        var subscription by Delegates.notNull<EntitySubscription>()

        beforeEachTest {
            entity = subject.create()
            subscription = subject.subscribe(Aspect(Bits(), Bits(), Bits()))
        }

        it("should only destroy if entity exists") {
            assertThat({
                subject.destroy(entity + 1)
            }, throws<IllegalArgumentException>())
        }

        on("entity destroy") {
            subject.destroy(entity)

            it("should not be in subscription#entities") {
                assertThat(subscription.entities().contains(entity), equalTo(false))
            }
        }
    }
})
