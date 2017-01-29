package org.pandaframework.ecs

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import com.nhaarman.mockito_kotlin.mock
import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.state.StateHandler
import org.pandaframework.ecs.support.TestState
import org.pandaframework.ecs.support.TestSystem

/**
 * @author Ranie Jade Ramiso
 */
object WorldBuilderSpec: SubjectSpek<WorldBuilder<TestState>>({
    subject { WorldBuilder() }

    it("requires an initial state") {
        assertThat({
            subject.build()
        }, throws<IllegalArgumentException>())
    }

    on("build") {
        val state1Handler = mock<StateHandler<TestState.State1>>()
        val state2Handler = mock<StateHandler<TestState.State2>>()
        val system = object: TestSystem() { }

        subject.initialState(TestState.State1)
        subject.registerStateHandler(TestState.State1, state1Handler)
        subject.registerStateHandler(TestState.State2, state2Handler)
        subject.registerSystem(system)

        val world = subject.build()

        it("should configure the StateHandlers") {
            expect(world.stateManager.handlers.values).contain.elements(state1Handler, state2Handler)
        }

        it("should configure the Systems") {
            expect(world.systems).contain.elements(system)
        }

        it("should configure the initial state") {
            expect(world.initialState)
        }
    }
})
