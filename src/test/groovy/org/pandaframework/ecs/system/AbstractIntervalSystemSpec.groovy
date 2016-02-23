package org.pandaframework.ecs.system

import org.pandaframework.ecs.TestPeer
import org.pandaframework.ecs.entity.Aspect
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.entity.EntitySubscriptionManager
import org.pandaframework.ecs.util.collection.IntBag
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Ranie Jade Ramiso
 */
class AbstractIntervalSystemSpec extends Specification {
    def peer
    def entitySubscriptionManager = Mock(EntitySubscriptionManager)

    def setup() {
        peer = new TestPeer(entitySubscriptionManager)
    }

    def "should only be updated on specified interval"(float[] deltas, float interval, int updateCount) {
        setup:
            def subscription = Mock(EntitySubscription)
            def bag = new IntBag()
            bag.insert(1)
            subscription.entities() >> bag
            entitySubscriptionManager.subscription(_) >> subscription

            def counter = new AtomicInteger()

            def system = new AbstractIntervalSystem(Aspect.any(), interval) {
                @Override
                protected void inserted(int entity) {

                }

                @Override
                protected void removed(int entity) {

                }

                @Override
                protected void process(float delta, int entity) {
                    counter.getAndIncrement()
                }
            }

            system.impl_setPeer(peer)

            system.initialize()

        when:
            deltas.each {
                system.process(it)
            }

        then:
            counter.intValue() == updateCount

        where:
            deltas                     | interval | updateCount
            [1000, 2000, 3000]         | 1000     | 3
            [1500, 1800, 1900]         | 500      | 3
            [500, 500, 1000, 300, 700] | 1500     | 2
    }
}
