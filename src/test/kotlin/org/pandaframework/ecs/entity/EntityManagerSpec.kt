package org.pandaframework.ecs.entity

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.pandaframework.ecs.component.DefaultComponentFactories
import org.pandaframework.ecs.component.DefaultComponentIdentityManager

/**
 * @author Ranie Jade Ramiso
 */
class EntityManagerSpec: SubjectSpek<EntityManager>({
    subject {
        DefaultEntitySubscriptionManager(DefaultComponentIdentityManager(), DefaultComponentFactories())
    }

    describe("entity creation") {
        it("should return unique ids") {
            assertThat(subject.create(), !equalTo(subject.create()))
        }
    }

    describe("entity removal") {
        it("should throw an exception if entity is not alive") {
            assertThat({
                subject.remove(1)
            }, throws<EntityNotFoundException>())
        }

        it("should delete alive entities") {
            val entity = subject.create()
            assertThat({
                subject.remove(entity)
            }, !throws<EntityNotFoundException>())
        }
    }
})
