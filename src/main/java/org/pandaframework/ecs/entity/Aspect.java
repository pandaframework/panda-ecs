package org.pandaframework.ecs.entity;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import org.pandaframework.ecs.component.Component;

/**
 * @author Ranie Jade Ramiso
 */
public class Aspect {
    private final Set<Class<? extends Component>> allComponents = new HashSet<>();
    private final Set<Class<? extends Component>> anyComponents = new HashSet<>();
    private final Set<Class<? extends Component>> excludeComponents = new HashSet<>();
    private final Builder builder = new Builder();

    public static Aspect.Builder all(Set<Class<? extends Component>> components) {
        return new Aspect().builder.all(components);
    }

    public static Aspect.Builder all(Class<? extends Component>... components) {
        return new Aspect().builder.all(components);
    }

    public static Aspect.Builder any(Set<Class<? extends Component>> components) {
        return new Aspect().builder.any(components);
    }

    public static Aspect.Builder any(Class<? extends Component>... components) {
        return new Aspect().builder.any(components);
    }

    public static Aspect.Builder exclude(Set<Class<? extends Component>> components) {
        return new Aspect().builder.exclude(components);
    }

    public static Aspect.Builder exclude(Class<? extends Component>... components) {
        return new Aspect().builder.exclude(components);
    }

    public final class Builder {

        public Aspect.Builder all(Set<Class<? extends Component>> components) {
            allComponents.addAll(components);
            return this;
        }

        public Aspect.Builder all(Class<? extends Component>... components) {
            return all(Sets.newHashSet(components));
        }

        public Aspect.Builder any(Set<Class<? extends Component>> components) {
            anyComponents.addAll(components);
            return this;
        }

        public Aspect.Builder any(Class<? extends Component>... components) {
            return any(Sets.newHashSet(components));
        }

        public Aspect.Builder exclude(Set<Class<? extends Component>> components) {
            excludeComponents.addAll(components);
            return this;
        }

        public Aspect.Builder exclude(Class<? extends Component>... components) {
            return exclude(Sets.newHashSet(components));
        }

        Aspect build() {
            return Aspect.this;
        }
    }

    Set<Class<? extends Component>> getAllComponents() {
        return allComponents;
    }

    Set<Class<? extends Component>> getAnyComponents() {
        return anyComponents;
    }

    Set<Class<? extends Component>> getExcludeComponents() {
        return excludeComponents;
    }

    private Aspect() {

    }
}
