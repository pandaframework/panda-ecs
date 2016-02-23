package org.pandaframework.ecs

import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.system.AbstractSystem
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Ranie Jade Ramiso
 */
class WorldUpdateSpec extends Specification {
    static def processed = new AtomicBoolean(false);
    WorldConfig.Builder config

    def setup() {
        config = new WorldConfig.Builder()
    }

    def "initialize first"() {
        setup:
            def world = new World(config.build())

        when:
            world.update(1000)

        then:
            thrown(WorldException)
    }


    def "process configured systems"() {
        setup:
            def config = config
                .addSystem(TestSystem)
                .build()
            def world = new World(config)
            world.initialize()

        when:
            world.update(1000)

        then:
            processed.get()
    }

    def "invalid delta"(float delta) {
        setup:
            def world = new World(config.build())
            world.initialize()

        when:
            world.update(delta)

        then:
            thrown(IllegalArgumentException)

        where:
            delta | _
            0     | _
            -1    | _
    }

    static class TestSystem extends AbstractSystem {

        public TestSystem() {
            super(Aspect.all())
        }

        @Override
        void process(float delta) {
            processed.set(true)
        }

        @Override
        protected void inserted(int entity) {

        }

        @Override
        protected void removed(int entity) {

        }

        @Override
        protected void process(float delta, int entity) {

        }
    }

}
