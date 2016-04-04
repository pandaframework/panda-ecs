package org.pandaframework.ecs

import org.pandaframework.ecs.entity.aspect
import org.pandaframework.ecs.system.AbstractSystem

open class TestSystem : AbstractSystem(aspect {}) {
    var initialized: Boolean = false
        private set
    var processable: Boolean = true
    var processed: Int = 0
    var destroyed: Boolean = false
        private set

    override fun initialize() {
        super.initialize()
        initialized = true
    }

    override fun process(delta: Float) {
        super.process(delta)
        processed++
    }

    override fun destroy() {
        super.destroy()
        destroyed = true
        initialized = false
    }

    override fun canProcess(delta: Float): Boolean {
        return processable
    }

    override fun process(entity: Int, delta: Float) {}
}

class TestSystem1: TestSystem()
class TestSystem2: TestSystem()
class TestSystem3: TestSystem()
class DisabledSystem: TestSystem() {
    init {
        enabled = false
    }
}

class NotProcessableSystem : TestSystem() {
    init {
        processable = false
    }
}
