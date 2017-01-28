package org.pandaframework.ecs

import org.pandaframework.ecs.aspect.AspectBuilder
import org.pandaframework.ecs.state.State
import org.pandaframework.ecs.state.StateHandler
import org.pandaframework.ecs.system.System

// EXAMPLES

sealed class MyStates: State {
    object State1: MyStates()
    object State2: MyStates()
}

class MySystem: System<MyStates>() {
    override val supportedStates: Array<MyStates>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun AspectBuilder.aspect() {
        TODO()
    }

}

class MyStateHander1: StateHandler<MyStates.State1>() {
    override fun setup() {
        TODO()
    }

    override fun cleanup() {
        TODO()
    }

}

class MyStateHander2: StateHandler<MyStates.State2>() {
    override fun setup() {
        TODO()
    }

    override fun cleanup() {
        TODO()
    }

}

fun main(vararg args: String) {

    val world = createWorld<MyStates> {
        registerSystem(MySystem())
        registerStateHandler(MyStates.State1, MyStateHander1())
        registerStateHandler(MyStates.State2, MyStateHander2())
    }

    world.setup()

    world.update(1000.0)

    world.cleanup()
}
