package org.pandaframework.ecs.system

interface BasicSystem {
    fun update(time: Double, entities: IntArray)
}

class BasicUpdateStrategy<out T>(val system: T): UpdateStrategy
    where T: System<*>, T: BasicSystem {

    override fun update(time: Double) {
        system.update(time, system.subscription().entities())
    }
}
