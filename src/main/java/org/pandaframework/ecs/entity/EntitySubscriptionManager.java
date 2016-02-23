package org.pandaframework.ecs.entity;

import org.pandaframework.ecs.util.collection.ImmutableIntBag;

/**
 * @author Ranie Jade Ramiso
 */
public class EntitySubscriptionManager {
    public EntitySubscription subscription(Aspect.Builder aspect) {
        // FIXME: implement
        return new EntitySubscription() {
            @Override
            public ImmutableIntBag entities() {
                return null;
            }

            @Override
            public void subscribe(Listener subscriber) {

            }

            @Override
            public void unsubscribe(Listener subscriber) {

            }
        };
    }
}
