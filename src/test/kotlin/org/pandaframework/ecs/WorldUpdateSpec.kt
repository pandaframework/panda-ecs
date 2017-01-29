package org.pandaframework.ecs

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.state.StateManager
import org.pandaframework.ecs.support.TestState
import org.pandaframework.ecs.system.System

/**
 * @author Ranie Jade Ramiso
 */
object WorldUpdateSpec: SubjectSpek<World<TestState>>({
    val system1 = memoized { mock<System<TestState>>() }
    val system2 = memoized { mock<System<TestState>>() }
    val entityManager = memoized { mock<EntityManager>() }
    val stateManager = memoized {
        mock<StateManager<TestState>> {
            on { currentState() } doReturn(TestState.State1)
        }
    }

    subject {
        World(entityManager(), stateManager(), TestState.State1, listOf(system1(), system2())).apply {
            setup()
        }
    }

    describe("supported states") {
        it("should only update systems supporting the current state") {
            whenever(system1().supportedStates) doReturn(arrayOf<TestState>(TestState.State1))
            whenever(system2().supportedStates) doReturn(arrayOf<TestState>(TestState.State2))

            val time = 1000.0
            subject.update(time)

            verify(system1()).update(eq(time))
            verify(system2(), never()).update(any())
        }

        it("should always update systems with empty supported states") {
            whenever(system1().supportedStates) doReturn(arrayOf<TestState>(TestState.State1))
            whenever(system2().supportedStates) doReturn(emptyArray())

            val time = 1000.0
            subject.update(time)

            verify(system1()).update(eq(time))
            verify(system2()).update(eq(time))
        }
    }
})
