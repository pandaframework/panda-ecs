package org.pandaframework.ecs;

import org.pandaframework.ecs.entity.EntitySubscriptionManager;
import org.pandaframework.ecs.system.AbstractSystem;

/**
 * @author Ranie Jade Ramiso
 */
public class World {

    public World(WorldConfig config) {
        AbstractSystem.impl_setPeer(new PeerImpl());
    }

    void update(float delta) {

    }

    private final class PeerImpl implements AbstractSystem.Peer {
        @Override
        public EntitySubscriptionManager getEntitySubscriptionManager() {
            return null;
        }
    }
}
