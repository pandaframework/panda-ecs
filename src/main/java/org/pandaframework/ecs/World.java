package org.pandaframework.ecs;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.pandaframework.ecs.entity.EntitySubscriptionManager;
import org.pandaframework.ecs.system.AbstractSystem;

/**
 * @author Ranie Jade Ramiso
 */
public class World {
    private final WorldConfig config;
    private boolean initialized = false;
    private final PeerImpl peer = new PeerImpl();
    private final Set<AbstractSystem> systems = new LinkedHashSet<>();

    public World(WorldConfig config) {
        this.config = config;
    }

    public void initialize() throws WorldException {
        initialized = true;

        try {
            config.getSystems().stream()
                .map(this::instantiate)
                .forEachOrdered(system -> {
                    system.impl_setPeer(peer);
                    system.initialize();
                    systems.add(system);
                });
        } catch (Exception e) {
            throw new WorldException("Failed to initialized world.", e);
        }
    }

    public void update(float delta) throws WorldException {
        assertInitialized();
        Preconditions.checkArgument(delta > 0, "Delta should be greater than 0");

        try {
            systems.stream()
                .forEachOrdered(system -> this.updateSystem(system, delta));
        } catch (Exception e) {
            throw new WorldException("Failed to update world.", e);
        }
    }

    public void destroy() {
        initialized = false;
        systems.stream()
            .forEachOrdered(AbstractSystem::destroy);
        systems.clear();
    }

    public boolean isInitialized() {
        return initialized;
    }

    private void updateSystem(AbstractSystem system, float delta) {
        try {
            system.process(delta);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private AbstractSystem instantiate(Class<? extends AbstractSystem> system) {
        try {
            return system.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Throwables.propagate(e);
        }
        return null;
    }

    private void assertInitialized() throws WorldException {
        if (!isInitialized()) {
            throw new WorldException("World not initialized!", null);
        }
    }

    private final class PeerImpl implements AbstractSystem.Peer {
        @Override
        public EntitySubscriptionManager getEntitySubscriptionManager() {
            return new EntitySubscriptionManager();
        }
    }
}
