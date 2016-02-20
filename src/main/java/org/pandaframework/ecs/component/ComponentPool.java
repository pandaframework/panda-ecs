package org.pandaframework.ecs.component;

/**
 * @author Ranie Jade Ramiso
 */
public interface ComponentPool<T extends Component> {
    T get() throws ComponentPoolException;
    void release(T component);
}
