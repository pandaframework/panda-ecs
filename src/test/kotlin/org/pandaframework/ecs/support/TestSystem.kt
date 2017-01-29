package org.pandaframework.ecs.support

import org.pandaframework.ecs.aspect.AspectBuilder
import org.pandaframework.ecs.system.System
import org.pandaframework.ecs.system.UpdateStrategy

/**
 * @author Ranie Jade Ramiso
 */
abstract class TestSystem: System<TestState>() {
    override val supportedStates: Array<TestState>
        get() = emptyArray()

    override fun AspectBuilder.aspect() {
        TODO()
    }

    override fun updateStrategy(): UpdateStrategy {
        TODO()
    }
}
