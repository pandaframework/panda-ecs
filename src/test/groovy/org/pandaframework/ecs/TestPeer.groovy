package org.pandaframework.ecs

import org.pandaframework.ecs.entity.EntitySubscriptionManager
import org.pandaframework.ecs.system.AbstractSystem

/**
 * @author Ranie Jade Ramiso
 */
class TestPeer implements AbstractSystem.Peer {
    private final EntitySubscriptionManager entitySubscriptionManager;

    TestPeer(EntitySubscriptionManager entitySubscriptionManager) {
        this.entitySubscriptionManager = entitySubscriptionManager;
    }

    @Override
    EntitySubscriptionManager getEntitySubscriptionManager() {
        return entitySubscriptionManager
    }
}
