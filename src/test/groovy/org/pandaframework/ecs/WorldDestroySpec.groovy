package org.pandaframework.ecs

import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.system.AbstractSystem
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Ranie Jade Ramiso
 */
class WorldDestroySpec extends Specification {
    static def destroyed = new AtomicBoolean(false);
    WorldConfig.Builder config

    def setup() {
        config = new WorldConfig.Builder()
    }

    def "mark as un-initialized"() {
        setup:
            def world = new World(config.build())
            world.initialize();

        when:
            world.destroy()

        then:
            !world.isInitialized()
    }

    def "destroy systems"() {
        setup:
            def config = config
                .addSystem(TestSystem)
                .build()

            def world = new World(config)
            world.initialize()

        when:
            world.destroy()

        then:
            destroyed.get()
    }

    static class TestSystem extends AbstractSystem {
        public TestSystem() {
            super(Aspect.all())
        }

        @Override
        void destroy() {
            super.destroy()
            destroyed.set(true);
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
