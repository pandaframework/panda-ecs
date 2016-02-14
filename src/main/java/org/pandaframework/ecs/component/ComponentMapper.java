package org.pandaframework.ecs.component;

/**
 * @author Ranie Jade Ramiso
 */
public interface ComponentMapper<T extends Component> {
    T get(int entity);
    void remove(int entity);
    boolean contains(int entity);
}
