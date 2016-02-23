package org.pandaframework.ecs;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pandaframework.ecs.system.AbstractSystem;

/**
 * @author Ranie Jade Ramiso
 */
public class WorldConfig {

    private final Set<Class<? extends AbstractSystem>> systems;

    public static final class Builder {
        private final Set<Class<? extends AbstractSystem>> systems = new LinkedHashSet<>();

        public Builder addSystem(Class<? extends AbstractSystem> system) {
            systems.add(system);
            return this;
        }

        public WorldConfig build() {
            return new WorldConfig(systems);
        }
    }

    private WorldConfig(Set<Class<? extends AbstractSystem>> systems) {
        this.systems = Collections.unmodifiableSet(systems);
    }

    public Set<Class<? extends AbstractSystem>> getSystems() {
        return systems;
    }
}
