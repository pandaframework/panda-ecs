package org.pandaframework.ecs.system

class IntervalUpdateStrategy(val interval: Double,
                             delegate: UpdateStrategy): ForwardingUpdateStrategy(delegate) {
    private var accumulator = 0.0

    override fun update(time: Double) {
        accumulator += time
        while (accumulator >= interval) {
            super.update(time)
            accumulator -= interval
        }
    }
}
