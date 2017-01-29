package org.pandaframework.ecs.system

object UpdateStrategies {
    fun <T> iterating(system: T): UpdateStrategy
        where T: System<*>, T: IteratingSystem {
        return IteratingUpdateStrategy(system)
    }

    fun <T> basic(system: T): UpdateStrategy
        where T: System<*>, T: BasicSystem {
        return BasicUpdateStrategy(system)
    }

    fun interval(interval: Double, strategy: UpdateStrategy): UpdateStrategy {
        return IntervalUpdateStrategy(interval, strategy)
    }
}
