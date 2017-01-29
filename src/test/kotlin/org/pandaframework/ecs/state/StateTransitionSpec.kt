package org.pandaframework.ecs.state

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.support.TestState

/**
 * @author Ranie Jade Ramiso
 */
object StateTransitionSpec: SubjectSpek<StateManager<TestState>>({
    val state1Handler = memoized { mock<StateHandler<TestState.State1>>() }
    val state2Handler = memoized { mock<StateHandler<TestState.State2>>() }
    val entityManager = memoized { mock<EntityManager>() }

    subject {
        StateManager(
            entityManager(),
            mapOf(
                TestState.State1 to state1Handler(),
                TestState.State2 to state2Handler()
            )
        )
    }

    describe("initial transition") {
        it("should setup the state") {
            subject.transitionTo(TestState.State1)
            subject.process()
            verify(state1Handler()).setup()
        }
    }

    on("transition to nothing") {
        subject.process()

        it("should not do anything") {
            verify(state1Handler(), never()).setup()
            verify(state1Handler(), never()).cleanup()

            verify(state2Handler(), never()).setup()
            verify(state2Handler(), never()).cleanup()
        }
    }

    describe("state transition") {
        beforeEachTest {
            subject.setup()
            subject.transitionTo(TestState.State1)
            subject.process()
        }

        on("change state") {
            subject.transitionTo(TestState.State2)

            it("should not happen immediately") {
                expect(subject.currentState()).to.be.equal(TestState.State1)
            }

            it("should only change when process is invoked") {
                subject.process()
                expect(subject.currentState()).to.be.equal(TestState.State2)
            }

            it("should clean up previous state") {
                verify(state1Handler()).cleanup()
            }

            it("should setup the next state") {
                verify(state2Handler()).setup()
            }
        }

        it("should not allow transitioning into same state") {
            assertThat({
                subject.transitionTo(TestState.State1)
            }, throws<IllegalArgumentException>())
        }
    }

    it("should setup the handlers") {
        subject.setup()

        // everything after '=' does not matter, we just want to trigger the call to the setter
        verify(state1Handler())._entityManager = entityManager()
        verify(state1Handler())._stateManager = subject
    }

    it("should cleanup the last state") {
        subject.setup()
        subject.transitionTo(TestState.State1)
        subject.process()

        subject.cleanup()
        verify(state1Handler()).cleanup()
    }
})
