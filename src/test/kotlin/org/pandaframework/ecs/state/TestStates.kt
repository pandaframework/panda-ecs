package org.pandaframework.ecs.state

/**
 * @author Ranie Jade Ramiso
 */

sealed class TestState: State {
    object State1: TestState()
    object State2: TestState()
}
