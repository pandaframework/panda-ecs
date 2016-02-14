package org.pandaframework.ecs.entity;

import org.pandaframework.ecs.util.collection.ImmutableIntBag;

/**
 * @author Ranie Jade Ramiso
 */
public interface EntitySubscription {
    ImmutableIntBag entities();
    void subscribe(Listener subscriber);
    void unsubscribe(Listener subscriber);

    interface Listener {
        void inserted(ImmutableIntBag entities);
        void removed(ImmutableIntBag entities);
    }
}
