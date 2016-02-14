package io.polymorphicpanda.panda.ecs.system;

import io.polymorphicpanda.panda.ecs.entity.Aspect;

/**
 * @author Ranie Jade Ramiso
 */
public abstract class AbstractIntervalSystem extends AbstractSystem {
    private final float interval;
    private float accumulator;

    protected AbstractIntervalSystem(Aspect aspect, float interval) {
        super(aspect);
        this.interval = interval;
    }

    @Override
    protected boolean canProcess(float delta) {
        accumulator += delta;

        if (accumulator >= interval) {
            accumulator -= interval;
            return true;
        }
        return false;
    }
}
