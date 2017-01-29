package org.pandaframework.ecs.system

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.support.TestSystem


/**
 * @author Ranie Jade Ramiso
 */
object IntervalUpdateStrategySpec: SubjectSpek<IntervalUpdateStrategy>({
    val interval = 1000.0
    val subscription = memoized { mock<EntitySubscription>() }

    val entityManager = memoized {
        mock<EntityManager> {
            on {
                subscribe(any())
            } doReturn subscription()
        }
    }

    val system = memoized {
        val system = object: TestSystem(), BasicSystem {
            var count = 0
            override fun update(time: Double, entities: IntArray) {
                count += 1
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
            IntervalUpdateStrategy(interval, BasicUpdateStrategy(this))
        }
    }

    it("should update when interval has passed") {
        whenever(subscription().entities()) doReturn(intArrayOf())
        subject.update(1000.0)

        expect(system().count).to.be.equal(1)
    }

    it("should update more than once depending on elapsed time") {
        whenever(subscription().entities()) doReturn(intArrayOf())

        subject.update(2500.00)
        expect(system().count).to.be.above(1)
    }
})
