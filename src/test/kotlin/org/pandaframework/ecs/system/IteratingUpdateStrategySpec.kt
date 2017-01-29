package org.pandaframework.ecs.system

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.entity.Entity
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.support.TestSystem


/**
 * @author Ranie Jade Ramiso
 */
object IteratingUpdateStrategySpec: SubjectSpek<IteratingUpdateStrategy<*>>({
    val subscription = memoized { mock<EntitySubscription>() }

    val entityManager = memoized {
        mock<EntityManager> {
            on {
                subscribe(any())
            } doReturn subscription()
        }
    }

    val system = memoized {
        val system = object: TestSystem(), IteratingSystem {
            val entities = mutableListOf<Int>()

            override fun update(time: Double, entity: Entity) {
                entities.add(entity)
            }

            override fun updateStrategy(): UpdateStrategy { TODO() }
        }

        system.apply {
            _entityManager = entityManager()
            bootstrap()
        }
    }

    subject {
        with(system()) {
            IteratingUpdateStrategy(this)
        }
    }

    it("should iterate each entity") {
        val entities = intArrayOf(1, 2, 3)
        whenever(subscription().entities()) doReturn(entities)
        subject.update(1000.0)

        expect(system().entities).satisfy {
            it.containsAll(entities.toList())
        }
    }
})
