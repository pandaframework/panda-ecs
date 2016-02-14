package org.pandaframework.ecs.system

import org.pandaframework.ecs.TestPeer
import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.entity.EntitySubscriptionManager
import org.pandaframework.ecs.util.collection.IntBag
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Ranie Jade Ramiso
 */
class SystemBasicSpec extends Specification {
    def entitySubscriptionManager = Mock(EntitySubscriptionManager)

    def setup() {
        AbstractSystem.impl_setPeer(new TestPeer(entitySubscriptionManager))
    }

    @Unroll
    def "should only be process when enabled and canProcess returns true"(boolean enabled, boolean canProcess) {
        setup:

            // defaults
            def subscription = Mock(EntitySubscription)
            def bag = new IntBag()
            bag.insert(1)
            subscription.entities() >> bag
            entitySubscriptionManager.subscription(_) >> subscription

            def system = new AbstractSystem(Aspect.any()) {
                def called = new AtomicBoolean(false)

                @Override
                void initialize() {
                    super.initialize()
                    setEnabled(enabled)
                }

                @Override
                protected void inserted(int entity) {
                }

                @Override
                protected void removed(int entity) {

                }

                @Override
                protected void process(float delta, int entity) {
                    called.set(true)
                }

                @Override
                protected boolean canProcess(float delta) {
                    return canProcess;
                }
            }

            system.initialize()

        when:
            system.process(1000)

        then:
            system.called.get() == (enabled && canProcess)

        where:
            enabled | canProcess
            true    | true
            false   | false
            true    | false
            false   | true
    }
}
