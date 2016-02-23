package org.pandaframework.ecs

import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.system.AbstractIntervalSystem
import org.pandaframework.ecs.system.AbstractSystem
import spock.lang.Specification

/**
 * @author Ranie Jade Ramiso
 */
class WorldConfigBuilderSpec extends Specification {

    class TestSystem1 extends AbstractIntervalSystem {

        public TestSystem1(Aspect.Builder aspect, float interval) {
            super(aspect, interval)
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

    class TestSystem2 extends AbstractSystem {

        public TestSystem2(Aspect.Builder aspect) {
            super(aspect)
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

    def "addSystem should preserve insertion order"() {
        when:
            def systems = new WorldConfig.Builder()
                .addSystem(TestSystem2)
                .addSystem(TestSystem1)
                .build().getSystems().iterator()



        then:
            systems.next() == TestSystem2
            systems.next() == TestSystem1
            !systems.hasNext()
    }
}
