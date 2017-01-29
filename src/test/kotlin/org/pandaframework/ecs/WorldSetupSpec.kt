package org.pandaframework.ecs

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.state.StateManager
import org.pandaframework.ecs.support.TestState
import org.pandaframework.ecs.system.System

/**
 * @author Ranie Jade Ramiso
 */
object WorldSetupSpec: SubjectSpek<World<TestState>>({
    val system1 = memoized { mock<System<TestState>>() }
    val system2 = memoized { mock<System<TestState>>() }
    val entityManager = memoized { mock<EntityManager>() }
    val stateManager = memoized { mock<StateManager<TestState>>() }

    subject {
        World(entityManager(), stateManager(), TestState.State1, listOf(system1(), system2()))
    }

    on("setup") {
        subject.setup()

        it("should setup StateManager") {
            verify(stateManager()).setup()
        }

        it("should transition to initial state") {
            verify(stateManager()).transitionTo(TestState.State1)
        }

        it("should bootstrap the registered systems") {
            verify(system1()).bootstrap()
            verify(system2()).bootstrap()
        }

        it("should setup the registered systems") {
            verify(system1()).setup()
            verify(system2()).setup()
        }
    }
})
